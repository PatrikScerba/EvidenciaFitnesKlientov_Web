import { useState } from "react";
import { login, getCurrentUser } from "../../api/authApi";

function Login({ onLoginSuccess }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await login({ email, password });
      const currentUser = await getCurrentUser();
      onLoginSuccess(currentUser);
    } catch (err) {
      setError(err.message || "Prihlásenie zlyhalo.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: "30px", fontFamily: "Arial" }}>
      <h2>Prihlásenie</h2>

      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: "15px" }}>
          <label htmlFor="email">Email</label>
          <br />
          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Zadaj email"
          />
        </div>

        <div style={{ marginBottom: "15px" }}>
          <label htmlFor="password">Heslo</label>
          <br />
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Zadaj heslo"
          />
        </div>

        {error && <p style={{ color: "red" }}>{error}</p>}

        <button type="submit" disabled={loading}>
          {loading ? "Prihlasujem..." : "Prihlásiť sa"}
        </button>
      </form>
    </div>
  );
}

export default Login;
