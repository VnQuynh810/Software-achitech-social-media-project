import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

const rateLimitedCount = new Counter('bi_chan_429');
const successCount = new Counter('thanh_cong_200');

export const options = {
    scenarios: {
        rate_limit_test: {
            executor: 'ramping-arrival-rate',
            startRate: 2,
            timeUnit: '1s',
            preAllocatedVUs: 20,
            maxVUs: 100,
            stages: [
                { target: 10, duration: '10s' },
                { target: 50, duration: '20s' },
                { target: 5, duration: '10s' },
            ],
        },
    },
};

export function setup() {
    const loginUrl = 'http://localhost:8080/api/auth/login';
    const payload = JSON.stringify({
        email: "nguyen@gmail.com",
        password: "1"
    });
    const params = {
        headers: { 'Content-Type': 'application/json' },
    };
    const res = http.post(loginUrl, payload, params);

    if (res.status !== 200) {
        console.error(`Login chết ngắt! Status: ${res.status}. Body: ${res.body}`);
        throw new Error('Không thể login để lấy token test.');
    }
    const token = res.body;
    return token;
}


export default function (token) {

    const targetUrl = 'http://localhost:8888/api/v1/auth/login';
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`, // Gắn Token vào Header
        },
    };
    const res = http.get(targetUrl, params);

    if (res.status === 429) {
        rateLimitedCount.add(1);
    } else if (res.status === 200) {
        successCount.add(1);
    }
}