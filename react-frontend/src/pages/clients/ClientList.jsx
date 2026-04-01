import { useEffect, useState } from "react";
import { getAllClients } from "../../api/clientApi";

export default function ClientList() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchClients();
  }, []);

  async function fetchClients() {
    setLoading(true);
    setError("");

    try {
      const data = await getAllClients();
      setClients(data);
    } catch (err) {
      setError(err.message || "Nepodarilo sa načítať klientov.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ marginTop: "20px" }}>
      <h2>Zoznam klientov</h2>

      {loading && <p>Načítavam...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && clients.length === 0 && <p>Žiadni klienti</p>}

      {!loading && clients.length > 0 && (
        <table border="1" cellPadding="8">
          <thead>
            <tr>
              <th>ID</th>
              <th>Meno</th>
              <th>Priezvisko</th>
              <th>Dátum narodenia</th>
              <th>Telefón</th>
              <th>Adresa</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {clients.map((client) => (
              <tr key={client.clientId}>
                <td>{client.clientId}</td>
                <td>{client.firstName}</td>
                <td>{client.lastName}</td>
                <td>{client.dateOfBirth}</td>
                <td>{client.phoneNumber}</td>
                <td>{client.address}</td>
                <td>{client.email}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
