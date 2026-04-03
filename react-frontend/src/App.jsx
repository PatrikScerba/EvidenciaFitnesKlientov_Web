import { useState } from "react";
import { logout } from "./api/authApi";
import Login from "./pages/auth/Login";
import ClientRegister from "./pages/ClientRegister";
import EmployeeRegister from "./pages/EmployeeRegister";
import ChangePassword from "./pages/auth/ChangePassword";
import AdminPasswordReset from "./pages/admin/AdminPasswordReset";
import ClientList from "./pages/clients/ClientList";
import ClientSearch from "./pages/clients/ClientSearch";
import { searchClients } from "./api/clientApi";
import ClientDetail from "./pages/clients/ClientDetail";
import ClientEdit from "./pages/clients/ClientEdit";

export default function App() {
  const [user, setUser] = useState(null);
  const [showClientRegister, setShowClientRegister] = useState(false);
  const [showEmployeeRegister, setShowEmployeeRegister] = useState(false);
  const [showChangePassword, setShowChangePassword] = useState(false);
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
      setShowChangePassword(false);
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
      setShowChangePassword(false);
      setShowAdminPasswordReset(false);
      setShowClientList(false);
      setShowClientSearch(false);
      setEditingClient(null);
    }
  }

  function handleShowChangePassword() {
    if (showChangePassword) {
      setShowChangePassword(false);
    } else {
      setShowChangePassword(true);
      setShowClientRegister(false);
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
      setShowChangePassword(false);
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
      setShowChangePassword(false);
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
      setShowChangePassword(false);
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
    setShowChangePassword(false);
    setShowAdminPasswordReset(false);
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
    } finally {
      setLoadingSearch(false);
    }
  }

  async function handleLogout() {
    try {
      await logout();
    } catch (err) {
    } finally {
      setUser(null);
      setShowClientRegister(false);
      setShowEmployeeRegister(false);
      setShowChangePassword(false);
      setShowAdminPasswordReset(false);
      setShowClientList(false);
      setShowClientSearch(false);
      setEditingClient(null);
    }
  }

  function renderDashboard() {
    if (user.role === "ADMIN") {
      return (
        <div>
          <h2>Admin panel</h2>
          <p>Prihlásený ako admin: {user.email}</p>

          <button
            style={{ marginRight: "10px" }}
            onClick={handleShowEmployeeForm}
          >
            {showEmployeeRegister
              ? "Zavrieť registráciu zamestnanca"
              : "Registrovať zamestnanca"}
          </button>

          <button
            style={{ marginRight: "10px" }}
            onClick={handleShowClientForm}
          >
            {showClientRegister
              ? "Zavrieť registráciu klienta"
              : "Registrovať klienta"}
          </button>

          <button onClick={handleShowAdminPasswordReset}>
            {showAdminPasswordReset
              ? "Zavrieť reset hesla"
              : "Reset hesla používateľa"}
          </button>

          <button onClick={handleShowClientList}>
            {showClientList ? "Zavrieť zoznam klientov" : "Zobraziť klientov"}
          </button>

          <button
            style={{ marginRight: "10px" }}
            onClick={handleShowClientSearch}
          >
            {showClientSearch
              ? "Zavrieť vyhľadávanie klientov"
              : "Vyhľadať klienta"}
          </button>

          {showClientList && (
            <div style={{ marginTop: "20px" }}>
              <ClientList
                onEdit={handleEditClient}
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
              <ClientSearch
                onSearch={handleClientSearch}
                loading={loadingSearch}
              />

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

    if (user.role === "EMPLOYEE") {
      return (
        <div>
          <h2>Zamestnanecký panel</h2>
          <p>Prihlásený ako zamestnanec: {user.email}</p>

          <button onClick={handleShowClientForm}>
            {showClientRegister ? "Zavrieť registráciu" : "Registrovať klienta"}
          </button>

          <button onClick={handleShowClientList}>
            {showClientList ? "Zavrieť zoznam klientov" : "Zobraziť klientov"}
          </button>

          <button
            style={{ marginRight: "10px" }}
            onClick={handleShowClientSearch}
          >
            {showClientSearch
              ? "Zavrieť vyhľadávanie klientov"
              : "Vyhľadať klienta"}
          </button>

          {showClientSearch && (
            <div style={{ marginTop: "20px" }}>
              <ClientSearch
                onSearch={handleClientSearch}
                loading={loadingSearch}
              />

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
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  ))}
              </div>
            </div>
          )}

          {showClientList && (
            <div style={{ marginTop: "20px" }}>
              <ClientList
                onEdit={handleEditClient}
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

          {showClientRegister && (
            <div style={{ marginTop: "20px" }}>
              <ClientRegister />
            </div>
          )}
        </div>
      );
    }

    return (
      <div>
        <h2>Klientsky panel</h2>
        <p>Prihlásený klient: {user.email}</p>
        <ClientDetail />
      </div>
    );
  }

  return (
    <div style={{ padding: "30px", fontFamily: "Arial" }}>
      {user ? (
        <div>
          <h1>React ↔ Spring Boot</h1>

          {renderDashboard()}

          {user.usingTemporaryPassword && (
            <div
              style={{
                marginTop: "20px",
                padding: "15px",
                border: "1px solid orange",
              }}
            >
              <p>Používate dočasné heslo. Môžete si ho zmeniť.</p>

              <button onClick={handleShowChangePassword}>
                {showChangePassword ? "Zavrieť zmenu hesla" : "Zmeniť heslo"}
              </button>
            </div>
          )}

          {showChangePassword && (
            <div style={{ marginTop: "20px" }}>
              <ChangePassword
                onPasswordChanged={(updatedUser) => {
                  setUser(updatedUser);
                  setShowChangePassword(false);
                }}
              />
            </div>
          )}

          <div style={{ marginTop: "20px" }}>
            <button onClick={handleLogout}>Odhlásiť sa</button>
          </div>
        </div>
      ) : (
        <Login onLoginSuccess={setUser} />
      )}
    </div>
  );
}
