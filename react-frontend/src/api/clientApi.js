import { apiFetch } from "./api";

export async function createClient(clientData) {
  return apiFetch("/api/clients", {
    method: "POST",
    body: JSON.stringify(clientData),
  });
}

export async function getAllClients() {
  return apiFetch("/api/clients");
}
