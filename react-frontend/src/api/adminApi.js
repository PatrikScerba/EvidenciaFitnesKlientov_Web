import { apiFetch } from "./api";

export async function createEmployee(employeeData) {
    return apiFetch("/api/admin/employees", {
        method: "POST",
        body: JSON.stringify(employeeData)
    });
}

export async function getSecurityQuestion(email) {
    return apiFetch(`/api/admin/users/security-question?email=${encodeURIComponent(email)}`, {
        method: "GET"
    });
}

export async function resetPasswordBySecurityAnswer(resetData) {
    return apiFetch("/api/admin/users/reset-password", {
        method: "POST",
        body: JSON.stringify(resetData)
    });
}
