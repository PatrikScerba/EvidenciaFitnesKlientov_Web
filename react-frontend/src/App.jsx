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

    return (

        <div style={{ padding: "30px", fontFamily: "Arial" }}>
            {user ? (
                <div>
                    <h1>React ↔ Spring Boot</h1>
                    <p>Používateľ je prihlásený.</p>
                    <pre>{JSON.stringify(user, null, 2)}</pre>

                    <button onClick={handleLogout}>
                        Odhlásiť sa
                    </button>
                </div>
            ) : (
                <Login onLoginSuccess={setUser} />
            )}
        </div>
    );
}
