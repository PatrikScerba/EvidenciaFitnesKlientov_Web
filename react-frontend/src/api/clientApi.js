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

export async function searchClients({
  firstName = "",
  lastName = "",
  email = "",
}) {
  const params = new URLSearchParams();

  if (firstName.trim()) {
    params.append("firstName", firstName.trim());
  }

  if (lastName.trim()) {
    params.append("lastName", lastName.trim());
  }

  if (email.trim()) {
    params.append("email", email.trim());
  }

  const queryString = params.toString();

  return apiFetch(
    queryString ? `/api/clients/search?${queryString}` : "/api/clients/search"
  );
}

export async function getMyClient() {
  return apiFetch("/api/clients/me");
}

export async function updateClient(id, clientData) {
  return apiFetch(`/api/clients/${id}`, {
    method: "PUT",
    body: JSON.stringify(clientData),
  });
}
