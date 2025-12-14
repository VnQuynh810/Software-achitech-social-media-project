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
                { target: 10, duration: '10s' }, // Tăng nhẹ
                { target: 50, duration: '20s' }, // Tấn công mạnh (Test Rate Limit)
                { target: 5, duration: '10s' },  // Giảm về
            ],
        },
    },
};

// --- BƯỚC 1: SETUP (Chạy 1 lần để lấy Token) ---
export function setup() {
    const loginUrl = 'http://localhost:8080/api/auth/login'; // Thay port nếu khác

    // Dựa trên class LoginRequest của bạn, JSON thường sẽ là email và password
    const payload = JSON.stringify({
        email: "nguyen@gmail.com",  // <-- Thay Email thật trong DB của bạn
        password: "1"      // <-- Thay Password thật
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const res = http.post(loginUrl, payload, params);

    // Debug: In ra nếu login lỗi
    if (res.status !== 200) {
        console.error(`Login chết ngắt! Status: ${res.status}. Body: ${res.body}`);
        throw new Error('Không thể login để lấy token test.');
    }

    // QUAN TRỌNG: Code Java trả về String trực tiếp, nên ta lấy res.body
    const token = res.body;
    return token;
}

// --- BƯỚC 2: CHẠY TEST (Dùng Token từ setup) ---
export default function (token) {

    const targetUrl = 'http://localhost:8888/api/v1/auth/login';

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`, // Gắn Token vào Header
        },
    };

    const res = http.get(targetUrl, params);

    // Kiểm tra kết quả
    if (res.status === 429) {
        rateLimitedCount.add(1); // Đếm số lần bị chặn
    } else if (res.status === 200) {
        successCount.add(1);
    }
}