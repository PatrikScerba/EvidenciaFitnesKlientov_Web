import { useEffect, useState } from "react";
import ClientDetail from "../clients/ClientDetail";
import { getMyMembership } from "../../api/membershipApi";

export default function ClientDashboard({ user }) {
  const [membership, setMembership] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchMembership() {
      try {
        const data = await getMyMembership();
        setMembership(data);
      } catch (error) {
        console.error("Chyba pri načítaní permanentky:", error.message);
      } finally {
        setLoading(false);
      }
    }

    fetchMembership();
  }, []);

  if (loading) {
    return <p>Načítavam permanentku...</p>;
  }

  return (
    <div>
      <h2>Klientsky panel</h2>
      <p>Prihlásený klient: {user.email}</p>

      <h3>Moja permanentka</h3>
      {membership ? (
        <div>
          <p>Status: {membership.status}</p>
          <p>Platná od: {membership.validFrom}</p>
          <p>Platná do: {membership.validTo}</p>
        </div>
      ) : (
        <p>Nemáš žiadnu permanentku.</p>
      )}

      <ClientDetail />
    </div>
  );
}
