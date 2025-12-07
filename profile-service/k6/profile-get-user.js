import http from 'k6/http';
import { check, sleep } from 'k6';
import { b64encode } from 'k6/encoding';

const BASE_URL = __ENV.PROFILE_BASE_URL || 'http://localhost:8083/profile';

export const options = {
  thresholds: {
    http_req_duration: ['p(95)<500'],
    checks: ['rate>0.95'],
  },
  scenarios: {
    getUserProfile: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 10 },
        { duration: '1m', target: 10 },
        { duration: '30s', target: 0 },
      ],
      gracefulRampDown: '10s',
    },
  },
};

function buildJwt(subject) {
  const now = Math.floor(Date.now() / 1000);
  const header = b64encode(JSON.stringify({ alg: 'HS256', typ: 'JWT' }), 'url');
  const payload = b64encode(
    JSON.stringify({
      sub: subject,
      scope: 'USER',
      iat: now,
      exp: now + 3600,
    }),
    'url',
  );

  return `${header}.${payload}.k6-load-test`;
}

function createProfile() {
  const userId = `k6-user-${Math.floor(Math.random() * 1e9)}`;
  const requestBody = JSON.stringify({
    userId,
    firstName: 'K6',
    lastName: 'LoadTest',
    dob: '1990-01-01',
    city: 'PerformanceCity',
  });

  const response = http.post(`${BASE_URL}/internal/users`, requestBody, {
    headers: { 'Content-Type': 'application/json' },
  });

  check(response, {
    'profile created': (res) => res.status === 200 && res.json('id'),
  });

  const profileId = response.json('id');
  const token = buildJwt(userId);

  return { profileId, token };
}

export function setup() {
  return createProfile();
}

export default function (data) {
  const response = http.get(`${BASE_URL}/users/${data.profileId}`, {
    headers: {
      Authorization: `Bearer ${data.token}`,
    },
  });

  check(response, {
    'retrieved profile': (res) => res.status === 200,
    'has profile payload': (res) => res.json('result.id') !== undefined,
  });

  sleep(1);
}
