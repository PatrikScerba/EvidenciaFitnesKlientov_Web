import { apiFetch } from "./api";

export async function getAdminClientQrCode(clientId) {
  return apiFetch(`/api/admin/client/${clientId}`, {
    method: "GET",
  });
}
