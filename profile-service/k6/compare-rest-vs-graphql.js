import http from 'k6/http';
import { check } from 'k6';

// --- CẤU HÌNH ---
// URL Profile Service
const PROFILE_URL = 'http://localhost:8083/profile';

// ID giả lập để test (Bạn có thể thay bằng ID có thật trong DB nếu setup lỗi)
const TEST_USER_ID = "1";

export const options = {
    scenarios: {
        test_rest_api: {
            executor: 'ramping-vus',
            exec: 'runRestApi',
            startVUs: 0,
            stages: [
                { duration: '5s', target: 10 }, // Tăng lên 10 user trong 5s
                { duration: '10s', target: 20 }, // Giữ 20 user trong 10s
                { duration: '5s', target: 0 },  // Giảm về 0
            ],
            startTime: '0s',
        },
        test_graphql: {
            executor: 'ramping-vus',
            exec: 'runGraphql',
            startVUs: 0,
            stages: [
                { duration: '5s', target: 10 },
                { duration: '10s', target: 20 },
                { duration: '5s', target: 0 },
            ],
            startTime: '0s',
        },
    },
    thresholds: {
        'http_req_failed': ['rate<0.01'], // Cho phép lỗi tối đa 1%
        'http_req_duration': ['p(95)<500'], // 95% request phải dưới 500ms
    },
};

// --- SETUP DỮ LIỆU (Chạy 1 lần đầu tiên) ---
export function setup() {
    console.log("--- TẠO DỮ LIỆU TEST ---");

    // Gọi API internal để tạo user (Vì đã permitAll nên không cần token)
    const createUrl = `${PROFILE_URL}/internal/users`;
    const payload = JSON.stringify({
        userId: TEST_USER_ID,
        id: TEST_USER_ID,
        firstName: "Benchmark",
        lastName: "User",
        city: "K6 City",
        dob: "2000-01-01"
    });

    const res = http.post(createUrl, payload, {
        headers: { 'Content-Type': 'application/json' }
    });

    if (res.status === 200 || res.status === 201) {
        console.log(`✅ Tạo user ${TEST_USER_ID} thành công.`);
    } else {
        console.log(`⚠️ Tạo user: ${res.status} (Có thể đã tồn tại).`);
    }

    return { userId: TEST_USER_ID };
}

// --- TEST REST: GET /users/{id} ---
export function runRestApi(data) {
    const url = `${PROFILE_URL}/users/${data.userId}`;

    // Không cần Header Authorization nữa
    const res = http.get(url, {
        headers: { 'Content-Type': 'application/json' },
        tags: { my_tag: 'REST' }
    });

    if (res.status !== 200) {
        console.error(`❌ REST Error: ${res.status}`);
    }
    check(res, { 'REST 200 OK': (r) => r.status === 200 });
}

// --- TEST GRAPHQL: query userProfile ---
export function runGraphql(data) {
    const url = `${PROFILE_URL}/graphql`;

    const query = `
        query GetUserProfile($id: String!) {
            userProfile(id: $id) {  
                id
                firstName
                lastName
                city
            }
        }
    `;

    const payload = JSON.stringify({
        query: query,
        variables: { id: String(data.userId) }
    });

    const res = http.post(url, payload, {
        headers: { 'Content-Type': 'application/json' },
        tags: { my_tag: 'GraphQL' }
    });

    if (res.status !== 200) {
        console.error(`❌ GraphQL Error: ${res.status}`);
    }
    check(res, { 'GraphQL 200 OK': (r) => r.status === 200 });
}