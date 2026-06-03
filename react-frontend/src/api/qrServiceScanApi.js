import { apiFetch } from "./api";

export async function  processQrServiceScan(qrToken) {
  return apiFetch("/api/entries/qr/service-scan", {
    method: "POST",
    body: JSON.stringify({ qrToken }),
  });
}
