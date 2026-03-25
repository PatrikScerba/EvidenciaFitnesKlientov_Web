import { apiFetch } from "./api";

export async function getCurrentUser() {
    return apiFetch("/api/admin/employees", {
        method: "GET"
    });
}
