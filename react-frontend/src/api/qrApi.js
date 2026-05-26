import { apiFetch } from "./api";

export async function getAdminClientQrCode(clientId) {
  return apiFetch(`/api/admin/client/${clientId}`, {
    method: "GET",
  });
}

export async function getEmployeeClientQrCode(data) {
  return apiFetch("/api/qr/show", {
    method: "POST",
    body: JSON.stringify(data),
  });
}

export async function getMyQr() {
  return apiFetch("/api/qr/me", {
    method: "GET",
  });
}