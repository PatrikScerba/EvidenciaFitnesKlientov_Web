import { apiFetch } from "./api";

export async function createEntry(entryData) {
  return apiFetch("/api/entries", {
    method: "POST",
    body: JSON.stringify(entryData),
  });
}

export async function registerDeparture(clientId) {
  return apiFetch(`/api/entries/${clientId}/departure`, {
    method: "PATCH",
  });
}

