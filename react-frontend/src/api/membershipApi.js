import { apiFetch } from "./api";

export async function createOrExtendMembership(membershipData) {
  return apiFetch("/api/memberships", {
    method: "POST",
    body: JSON.stringify(membershipData),
  });
}

export async function getMembershipByClientId(clientId) {
  return apiFetch(`/api/memberships/client/${clientId}`);
}

export async function getMyMembership() {
  return apiFetch("/api/memberships/me");
}


