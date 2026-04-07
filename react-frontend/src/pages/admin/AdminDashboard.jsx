import { useState } from "react";
import ClientRegister from "../clients/ClientRegister";
import EmployeeRegister from "../employees/EmployeeRegister";
import AdminPasswordReset from "./AdminPasswordReset";
import ClientList from "../clients/ClientList";
import ClientSearch from "../clients/ClientSearch";
import ClientEdit from "../clients/ClientEdit";
import { searchClients, deleteClient } from "../../api/clientApi";

export default function AdminDashboard({ user }) {
  const [showClientRegister, setShowClientRegister] = useState(false);
  const [showEmployeeRegister, setShowEmployeeRegister] = useState(false);
  const [showAdminPasswordReset, setShowAdminPasswordReset] = useState(false);
  const [showClientList, setShowClientList] = useState(false);
  const [showClientSearch, setShowClientSearch] = useState(false);
  const [searchResults, setSearchResults] = useState([]);
  const [loadingSearch, setLoadingSearch] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [editingClient, setEditingClient] = useState(null);
  const [clientListRefresh, setClientListRefresh] = useState(0);

  function handleShowEmployeeForm() {
    if (showEmployeeRegister) {
      setShowEmployeeRegister(false);
      setShowAdminPasswordReset(false);
    } else {
      setShowEmployeeRegister(true);
      setShowClientRegister(false);
      setShowAdminPasswordReset(false);
      setShowClientList(false);
      setShowClientSearch(false);
      setEditingClient(null);
    }
  }

  function handleShowClientForm() {
    if (showClientRegister) {
      setShowClientRegister(false);
    } else {
      setShowClientRegister(true);
      setShowEmployeeRegister(false);
      setShowAdminPasswordReset(false);
      setShowClientList(false);
      setShowClientSearch(false);
      setEditingClient(null);
    }
  }

  function handleShowAdminPasswordReset() {
    if (showAdminPasswordReset) {
      setShowAdminPasswordReset(false);
    } else {
      setShowAdminPasswordReset(true);
      setShowClientRegister(false);
      setShowEmployeeRegister(false);
      setShowClientList(false);
      setShowClientSearch(false);
      setEditingClient(null);
    }
  }

  function handleShowClientList() {
    if (showClientList) {
      setShowClientList(false);
      setEditingClient(null);
    } else {
      setShowClientList(true);
      setShowAdminPasswordReset(false);
      setShowClientRegister(false);
      setShowEmployeeRegister(false);
      setShowClientSearch(false);
      setEditingClient(null);
    }
  }

  function handleShowClientSearch() {
    if (showClientSearch) {
      setShowClientSearch(false);
      setSearchResults([]);
      setHasSearched(false);
    } else {
      setShowClientSearch(true);
      setShowClientRegister(false);
      setShowEmployeeRegister(false);
      setShowAdminPasswordReset(false);
      setShowClientList(false);
      setSearchResults([]);
      setHasSearched(false);
      setEditingClient(null);
    }
  }

  function handleEditClient(client) {
    setEditingClient(client);
    setShowClientSearch(false);
    setShowClientRegister(false);
    setShowEmployeeRegister(false);
    setShowAdminPasswordReset(false);
  }

  function handleClientDeleted() {
    setEditingClient(null);
    setShowClientList(true);
    setClientListRefresh((prev) => prev + 1);
  }

  async function handleClientSearch(filters) {
    if (filters === null) {
      setSearchResults([]);
      setHasSearched(false);
      return;
    }

    try {
      setLoadingSearch(true);
      setHasSearched(true);

      const data = await searchClients(filters);
      setSearchResults(data);
    } catch (err) {
      setSearchResults([]);
      console.error("Vyhľadávanie klientov zlyhalo:", err);
    } finally {
      setLoadingSearch(false);
    }
  }

  async function handleDeleteClient(id) {
    const confirmed = window.confirm("Naozaj chcete vymazať klienta?");
    if (!confirmed) return;

    try {
      await deleteClient(id);

      setSearchResults((prev) =>
        prev.filter((client) => client.clientId !== id)
      );

      setClientListRefresh((prev) => prev + 1);
      setEditingClient(null);
    } catch (err) {
      console.error("Mazanie klienta zlyhalo:", err);
    }
  }

  return (
    <div>
      <h2>Admin panel</h2>
      <p>Prihlásený ako admin: {user.email}</p>

      <button style={{ marginRight: "10px" }} onClick={handleShowEmployeeForm}>
        {showEmployeeRegister
          ? "Zavrieť registráciu zamestnanca"
          : "Registrovať zamestnanca"}
      </button>

      <button style={{ marginRight: "10px" }} onClick={handleShowClientForm}>
        {showClientRegister
          ? "Zavrieť registráciu klienta"
          : "Registrovať klienta"}
      </button>

      <button
        style={{ marginRight: "10px" }}
        onClick={handleShowAdminPasswordReset}
      >
        {showAdminPasswordReset
          ? "Zavrieť reset hesla"
          : "Reset hesla používateľa"}
      </button>

      <button style={{ marginRight: "10px" }} onClick={handleShowClientList}>
        {showClientList ? "Zavrieť zoznam klientov" : "Zobraziť klientov"}
      </button>

      <button onClick={handleShowClientSearch}>
        {showClientSearch
          ? "Zavrieť vyhľadávanie klientov"
          : "Vyhľadať klienta"}
      </button>

      {showClientList && (
        <div style={{ marginTop: "20px" }}>
          <ClientList
            onEdit={handleEditClient}
            onDeleteSuccess={handleClientDeleted}
            refreshKey={clientListRefresh}
          />
        </div>
      )}

      {editingClient && (
        <div style={{ marginTop: "20px" }}>
          <ClientEdit
            client={editingClient}
            onCancel={() => setEditingClient(null)}
            onUpdated={() => {
              setEditingClient(null);
              setShowClientList(true);
              setClientListRefresh((prev) => prev + 1);
            }}
          />
        </div>
      )}

      {showClientSearch && (
        <div style={{ marginTop: "20px" }}>
          <ClientSearch onSearch={handleClientSearch} loading={loadingSearch} />

          <div style={{ marginTop: "20px" }}>
            {hasSearched &&
              (searchResults.length === 0 ? (
                <p>Nenašli sa žiadni klienti.</p>
              ) : (
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
                    {searchResults.map((client) => (
                      <tr key={client.clientId}>
                        <td>{client.clientId}</td>
                        <td>{client.firstName}</td>
                        <td>{client.lastName}</td>
                        <td>{client.dateOfBirth}</td>
                        <td>{client.phoneNumber}</td>
                        <td>{client.address}</td>
                        <td>{client.email}</td>
                        <td>
                          <button onClick={() => handleEditClient(client)}>
                            Upraviť údaje
                          </button>

                          <button
                            style={{ marginLeft: "10px", color: "red" }}
                            onClick={() => handleDeleteClient(client.clientId)}
                          >
                            Vymazať
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ))}
          </div>
        </div>
      )}

      {showEmployeeRegister && (
        <div style={{ marginTop: "20px" }}>
          <EmployeeRegister />
        </div>
      )}

      {showClientRegister && (
        <div style={{ marginTop: "20px" }}>
          <ClientRegister />
        </div>
      )}

      {showAdminPasswordReset && (
        <div style={{ marginTop: "20px" }}>
          <AdminPasswordReset />
        </div>
      )}
    </div>
  );
}
