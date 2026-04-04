import { useEffect, useState } from "react";
import { getAllClients, deleteClient } from "../../api/clientApi";

export default function ClientList({ onEdit, onDeleteSuccess, refreshKey }) {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchClients();
  }, [refreshKey]);

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

  async function handleDelete(id) {
    const confirmed = window.confirm("Naozaj chceš vymazať klienta?");
    if (!confirmed) return;

    try {
      await deleteClient(id);

      setClients((prev) => prev.filter((client) => client.clientId !== id));
      if (onDeleteSuccess) {
        onDeleteSuccess();
      }
    } catch (err) {
      setError(err.message || "Vymazanie klienta zlyhalo.");
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
              <th>Akcie</th>
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
                <td>
                  <button onClick={() => onEdit(client)}>Upraviť</button>
                  <button
                    style={{ marginLeft: "10px", color: "red" }}
                    onClick={() => handleDelete(client.clientId)}
                  >
                    Vymazať
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
