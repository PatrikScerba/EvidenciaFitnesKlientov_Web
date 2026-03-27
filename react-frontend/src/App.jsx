import { useState } from "react";
import { logout } from "./api/authApi";
import Login from "./pages/auth/Login";
import ClientRegister from "./pages/ClientRegister";
import EmployeeRegister from "./pages/EmployeeRegister";
import ChangePassword from "./pages/auth/ChangePassword";

export default function App() {
    const [user, setUser] = useState(null);
    const [showClientRegister, setShowClientRegister] = useState(false);
    const [showEmployeeRegister, setShowEmployeeRegister] = useState(false);
    const [showChangePassword, setShowChangePassword] = useState(false);

    function handleShowEmployeeForm() {
        if (showEmployeeRegister) {
            setShowEmployeeRegister(false);
        } else {
            setShowEmployeeRegister(true);
            setShowClientRegister(false);
        }
    }

    function handleShowClientForm() {
        if (showClientRegister) {
            setShowClientRegister(false);
        } else {
            setShowClientRegister(true);
            setShowEmployeeRegister(false);
        }
    }

    function handleShowChangePassword() {
        setShowChangePassword(!showChangePassword);
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

                    <button onClick={handleShowClientForm}>
                        {showClientRegister
                            ? "Zavrieť registráciu klienta"
                            : "Registrovať klienta"}
                    </button>

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
                </div>
            );
        }

        if (user.role === "EMPLOYEE") {
            return (
                <div>
                    <h2>Zamestnanecký panel</h2>
                    <p>Prihlásený ako zamestnanec: {user.email}</p>

                    <button onClick={handleShowClientForm}>
                        {showClientRegister
                            ? "Zavrieť registráciu"
                            : "Registrovať klienta"}
                    </button>

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
                                border: "1px solid orange"
                            }}
                        >
                            <p>Používate dočasné heslo. Môžete si ho zmeniť.</p>

                            <button onClick={handleShowChangePassword}>
                                {showChangePassword
                                    ? "Zavrieť zmenu hesla"
                                    : "Zmeniť heslo"}
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
