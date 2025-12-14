import http from 'k6/http';
import { check, sleep } from 'k6';

// --- CẤU HÌNH ---
// URL của Profile Service (Cổng 8083, context-path /profile)
const BASE_URL = 'http://localhost:8083/profile';

// --- QUAN TRỌNG: ID LẤY TỪ HÌNH ẢNH CỦA BẠN ---
const TARGET_USER_ID = "f0b4058a-c6ef-4f2f-895d-72f093c27909";

export const options = {
    scenarios: {
        benchmark_mysql: {
            executor: 'ramping-vus', // Tăng dần lượng user ảo
            startVUs: 0,
            stages: [
                { duration: '5s', target: 100 },   // Khởi động nhẹ: 5 user
                { duration: '10s', target: 200 }, // Tải cao: 20 user dồn dập
                { duration: '10s', target: 2000 }, // Duy trì 20 user trong 10s
                { duration: '5s', target: 0 },   // Giảm dần về 0
            ],
            gracefulRampDown: '5s',
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<500'], // 95% request phải nhanh hơn 500ms
        checks: ['rate>0.99'], // Tỷ lệ thành công > 99%
    },
};

export default function () {
    // --- 1. TEST REST API ---
    // URL sẽ là: http://localhost:8083/profile/users/f0b4058a...
    const restRes = http.get(`${BASE_URL}/users/${TARGET_USER_ID}`, {
        headers: { 'Content-Type': 'application/json' },
        tags: { my_tag: 'REST' }
    });

    // Debug: Nếu lỗi thì in ra ngay
    if (restRes.status !== 200) {
        console.error(`❌ REST Error [${restRes.status}]: ${restRes.body}`);
    }

    check(restRes, {
        'REST 200 OK': (r) => r.status === 200,
        'REST Data Correct': (r) => r.body.includes(TARGET_USER_ID) // Check xem body có chứa ID không
    });

    // --- 2. TEST GRAPHQL (Optional - Nếu bạn muốn test luôn) ---
    /*
    const gqlQuery = `
        query {
            userProfile(id: "${TARGET_USER_ID}") {
                id
                firstName
                lastName
                city
            }
        }
    `;

    const gqlRes = http.post(`${BASE_URL}/graphql`, JSON.stringify({ query: gqlQuery }), {
        headers: { 'Content-Type': 'application/json' },
        tags: { my_tag: 'GraphQL' }
    });

    check(gqlRes, {
        'GraphQL 200 OK': (r) => r.status === 200,
        'GraphQL No Errors': (r) => !r.body.includes("errors")
    });
    */

    sleep(1);
}