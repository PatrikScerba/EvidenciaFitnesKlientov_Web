import { useState } from "react";
import { changePassword, getCurrentUser } from "../../api/authApi";

function ChangePassword({ onPasswordChanged }) {
    const [oldPassword, setOldPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {
        e.preventDefault();
        setError("");
        setMessage("");
        setLoading(true);

        try {
            await changePassword({
                oldPassword,
                newPassword,
                confirmPassword
            });

            const updatedUser = await getCurrentUser();

            setMessage("Heslo bolo úspešne zmenené.");
            setOldPassword("");
            setNewPassword("");
            setConfirmPassword("");

            onPasswordChanged(updatedUser);
        } catch (err) {
            setError(err.message || "Zmena hesla zlyhala.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <div style={{ marginTop: "20px", padding: "20px", border: "1px solid #ccc" }}>
            <h3>Zmena hesla</h3>

            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: "15px" }}>
                    <label htmlFor="oldPassword">Pôvodné heslo</label>
                    <br />
                    <input
                        id="oldPassword"
                        type="password"
                        value={oldPassword}
                        onChange={(e) => setOldPassword(e.target.value)}
                        placeholder="Zadajte pôvodné heslo"
                    />
                </div>

                <div style={{ marginBottom: "15px" }}>
                    <label htmlFor="newPassword">Nové heslo</label>
                    <br />
                    <input
                        id="newPassword"
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        placeholder="Zadajte nové heslo"
                    />
                </div>

                <div style={{ marginBottom: "15px" }}>
                    <label htmlFor="confirmPassword">Potvrdenie nového hesla</label>
                    <br />
                    <input
                        id="confirmPassword"
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        placeholder="Zadajte nové heslo znova"
                    />
                </div>

                {error && <p style={{ color: "red" }}>{error}</p>}
                {message && <p style={{ color: "green" }}>{message}</p>}

                <button type="submit" disabled={loading}>
                    {loading ? "Ukladám..." : "Zmeniť heslo"}
                </button>
            </form>
        </div>
    );
}

export default ChangePassword;