(function (global) {
    const API_BASE_URL = 'http://localhost:8080/turnforge/api/v1';

    async function request(endpoint, method = 'GET', body = null, token = null) {
        const headers = { 'Content-Type': 'application/json' };
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, {
                method,
                headers,
                body: body ? JSON.stringify(body) : null
            });

            const contentType = response.headers.get("content-type");
            const isJson = contentType && contentType.includes("application/json");

            let data = {};
            let text = "";

            if (response.status !== 204 && response.headers.get("content-length") !== "0") {
                text = await response.text();

                if (isJson) {
                    try {
                        data = JSON.parse(text);
                    } catch (e) {
                        data = { message: "Ошибка парсинга JSON ответа сервера" };
                    }
                } else {
                    data = { message: `Ошибка сервера (HTTP ${response.status}).`, text };
                }
            }

            if (!response.ok) {
                if (response.status === 404) {
                    return { data: {}, status: 404 };
                }

                throw {
                    response: {
                        status: response.status,
                        data: data
                    },
                    message: data.message || data.text || `Ошибка ${response.status}`
                };
            }

            return { data, status: response.status };

        } catch (e) {
            if (e.response) {
                throw e;
            }
            throw new Error(`Сетевая ошибка: ${e.message}`);
        }
    }

    global.API = { request };

})(window);

