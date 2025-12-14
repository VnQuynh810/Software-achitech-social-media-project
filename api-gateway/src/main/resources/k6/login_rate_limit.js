import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

const rateLimitedCount = new Counter('bi_chan_429');
const successCount = new Counter('thanh_cong_200');

export const options = {
    scenarios: {
        login_flood: {
            executor: 'ramping-arrival-rate',
            startRate: 2,
            timeUnit: '1s',
            preAllocatedVUs: 20,
            maxVUs: 100,
            stages: [
                { target: 10, duration: '10s' }, // Giai đoạn 1: Login chậm
                { target: 50, duration: '20s' }, // Giai đoạn 2: Spam login liên tục
                { target: 5, duration: '10s' },  // Giai đoạn 3: Giảm dần
            ],
        },
    },
};

export default function () {
    // 1. Target URL: Phải là địa chỉ của API Gateway (Port 8888 của bạn)
    const url = 'http://localhost:8888/api/v1/auth/login';

    // 2. Payload: Giả lập gửi user/pass liên tục
    const payload = JSON.stringify({
        email: "nguyen@gmail.com",
        password: "123456"
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
            // KHÔNG gửi Authorization header (vì đang đi login mà)
        },
    };

    // 3. Gửi POST Request (Login là POST)
    const res = http.post(url, payload, params);

    // 4. Kiểm tra
    if (res.status === 429) {
        rateLimitedCount.add(1); // Gateway chặn thành công
    } else if (res.status === 200) {
        successCount.add(1);     // Login thành công (chưa bị chặn)
    } else {
        // Log ra nếu gặp lỗi lạ (ví dụ 401 do sai pass, 500 lỗi server)
        console.log(`Status khác: ${res.status}`);
    }
}