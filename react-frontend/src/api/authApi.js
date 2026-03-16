import { apiFetch } from "./api";

export async function login(loginData) {
    return apiFetch("/api/auth/login", {
        method: "POST",
        body: JSON.stringify(loginData)
    });
}

export async function logout() {
    return apiFetch("/api/auth/logout", {
        method: "POST"
    });
}

export async function getCurrentUser() {
    return apiFetch("/api/auth/me", {
        method: "GET"
    });
}






