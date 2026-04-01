import { useState } from "react";
import { logout } from "./api/authApi";
import Login from "./pages/auth/Login";
import ClientRegister from "./pages/ClientRegister";
import EmployeeRegister from "./pages/EmployeeRegister";
import ChangePassword from "./pages/auth/ChangePassword";
import AdminPasswordReset from "./pages/admin/AdminPasswordReset";
import ClientList from "./pages/clients/ClientList";

export default function App() {
  const [user, setUser] = useState(null);
  const [showClientRegister, setShowClientRegister] = useState(false);
  const [showEmployeeRegister, setShowEmployeeRegister] = useState(false);
  const [showChangePassword, setShowChangePassword] = useState(false);
  const [showAdminPasswordReset, setShowAdminPasswordReset] = useState(false);
  const [showClientList, setShowClientList] = useState(false);

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
    }
  }

  function handleShowClientList() {
    if (showClientList) {
      setShowClientList(false);
    } else {
      setShowClientList(true);
      setShowAdminPasswordReset(false);
      setShowClientRegister(false);
      setShowEmployeeRegister(false);
      setShowChangePassword(false);
    }
  }

  async function handleLogout() {
    try {
      await logout();
    } catch (err) {
      console.error("Odhlásenie zlyhalo:", err);
    } finally {
      setUser(null);
      setShowClientRegister(false);
      setShowEmployeeRegister(false);
      setShowChangePassword(false);
      setShowAdminPasswordReset(false);
      setShowClientList(false);
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

          {showClientList && (
            <div style={{ marginTop: "20px" }}>
              <ClientList />
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

          {showClientList && (
            <div style={{ marginTop: "20px" }}>
              <ClientList />
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
        <pre>{JSON.stringify(user, null, 2)}</pre>
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
