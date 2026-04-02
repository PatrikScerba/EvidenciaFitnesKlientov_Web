import { useEffect, useState } from "react";
import { getMyClient } from "../../api/clientApi";

export default function ClientDetail() {
  const [client, setClient] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    async function fetchClient() {
      try {
        const data = await getMyClient();
        console.log("CLIENT DETAIL DATA:", data);
        setClient(data);
      } catch (err) {
        console.error("CLIENT DETAIL ERROR:", err);
        setErrorMessage(err.message || "Neznáma chyba pri načítaní klienta.");
      } finally {
        setLoading(false);
      }
    }

    fetchClient();
  }, []);

  if (loading) return <p>Načítavam údaje...</p>;
  if (errorMessage) return <p>Chyba: {errorMessage}</p>;
  if (!client) return <p>Klient nebol načítaný.</p>;

  return (
    <div>
      <h2>Moje údaje</h2>
      <p><strong>Krstné meno:</strong> {client.firstName}</p>
      <p><strong>Priezvisko:</strong> {client.lastName}</p>
      <p><strong>Adresa:</strong> {client.address}</p>
      <p><strong>Dátum narodenia:</strong> {client.dateOfBirth}</p>
      <p><strong>Telefón:</strong> {client.phoneNumber}</p>
      <p><strong>Email:</strong> {client.email}</p>
</div>
  );
}

