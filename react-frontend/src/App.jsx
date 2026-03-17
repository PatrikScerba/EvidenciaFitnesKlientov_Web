import { useState } from "react";
import Login from "./pages/auth/Login";

export default function App() {
    const [user, setUser] = useState(null);

    return (
        <div style={{ padding: "30px", fontFamily: "Arial" }}>
            {user ? (
                <div>
                    <h1>React ↔ Spring Boot</h1>
                    <p>Používateľ je prihlásený.</p>
                    <pre>{JSON.stringify(user, null, 2)}</pre>
                </div>
            ) : (
                <Login onLoginSuccess={setUser} />
            )}
        </div>
    );
}