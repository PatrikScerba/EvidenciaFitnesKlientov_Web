import { useEffect, useState } from "react";
import { getCurrentUser, logout } from "./api/authApi";
import Login from "./pages/auth/Login";
import ChangePassword from "./pages/auth/ChangePassword";
import AdminDashboard from "./pages/admin/AdminDashboard";
import EmployeeDashboard from "./pages/employees/EmployeeDashboard";
import ClientDashboard from "./pages/clients/ClientDashboard";

export default function App() {
  const [user, setUser] = useState(null);
  const [authLoading, setAuthLoading] = useState(true);
  const [showChangePassword, setShowChangePassword] = useState(false);

  useEffect(() => {
    async function checkAuth() {
      try {
        const currentUser = await getCurrentUser();
        setUser(currentUser);
      } catch (err) {
        setUser(null);
      } finally {
        setAuthLoading(false);
      }
    }

    checkAuth();
  }, []);

  function handleShowChangePassword() {
    setShowChangePassword((prev) => !prev);
  }

  async function handleLogout() {
    try {
      await logout();
    } catch (err) {
      console.error("Odhlásenie zlyhalo:", err);
    } finally {
      setUser(null);
      setShowChangePassword(false);
    }
  }

  function renderDashboard() {
    if (user.role === "ADMIN") {
      return <AdminDashboard user={user} />;
    }

    if (user.role === "EMPLOYEE") {
      return <EmployeeDashboard user={user} />;
    }

    return <ClientDashboard user={user} />;
  }

  if (authLoading) {
    return (
      <div style={{ padding: "30px", fontFamily: "Arial" }}>
        <h2>Načítavam prihlásenie...</h2>
      </div>
    );
  }

  if (!user) {
    return <Login onLoginSuccess={setUser} />;
  }

  return (
    <div style={{ padding: "30px", fontFamily: "Arial" }}>
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
  );
}
