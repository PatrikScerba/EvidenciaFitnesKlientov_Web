export default function ClientResultsTable({
  clients,
  onEdit,
  onDelete,
  onManageMembership,
  onManageEntry,
}) {
  return (
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
              <button onClick={() => onEdit(client)}>Upraviť údaje</button>

              <button
                style={{ marginLeft: "10px", color: "red" }}
                onClick={() => onDelete(client.clientId)}
              >
                Vymazať
              </button>

              <button
                style={{ marginLeft: "10px" }}
                onClick={() => onManageMembership(client)}
              >
                Permanentka
              </button>

              <button
                style={{ marginLeft: "10px" }}
                onClick={() => onManageEntry(client)}
              >
                Vstup
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
