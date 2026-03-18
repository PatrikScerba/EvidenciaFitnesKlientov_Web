import { useState } from "react";
import { logout } from "./api/authApi";
import Login from "./pages/auth/Login";

export default function App() {
    const [user, setUser] = useState(null);

    async function handleLogout() {
        try {
            await logout();
        } catch (err) {
            console.error("Odhlásenie zlyhalo:", err);
        } finally {
            setUser(null);
        }
    }

    function renderDashboard() {
        if (user.role === "ADMIN") {
            return (
                <div>
                    <h2>Admin panel</h2>
                    <p>Prihlásený ako admin: {user.email}</p>
                    <button>Registrovať zamestnanca</button>
                    <button>Registrovať klienta</button>
                </div>
            );
        }

        if (user.role === "EMPLOYEE") {
            return (
                <div>
                    <h2>Zamestnanecký panel</h2>
                    <p>Prihlásený ako zamestnanec: {user.email}</p>
                    <button>Registrovať klienta</button>
                </div>
            );
        }

        return (
            <div>
                <h2>Klientsky panel</h2>
                <p>Prihlásený klient: {user.email}</p>
                <button>Zobraziť moje údaje</button>
            </div>
        );
    }

    return (
        <div style={{ padding: "30px", fontFamily: "Arial" }}>
            {user ? (
                <div>
                    <h1>React ↔ Spring Boot</h1>
                    {renderDashboard()}

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
