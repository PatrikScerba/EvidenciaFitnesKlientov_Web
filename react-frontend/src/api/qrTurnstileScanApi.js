import { apiFetch } from "./api";

export async function processQrTurnstileScan(qrToken) {
  return apiFetch("/api/entries/scan", {
    method: "POST",
    body: JSON.stringify({ qrToken }),
  });
}
