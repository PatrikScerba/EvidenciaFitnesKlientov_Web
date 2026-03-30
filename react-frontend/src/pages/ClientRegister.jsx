import { useState } from "react";

export default function ClientRegister() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    phoneNumber: "",
    address: "",
    email: "",
    securityQuestion: "",
    securityAnswer: "",
    confirmSecurityAnswer: "",
  });

  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setResult(null);
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/clients", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Registrácia klienta zlyhala.");
      }

      const data = await response.json();
      setResult(data);

      setForm({
        firstName: "",
        lastName: "",
        dateOfBirth: "",
        phoneNumber: "",
        address: "",
        email: "",
        securityQuestion: "",
        securityAnswer: "",
        confirmSecurityAnswer: "",
      });
    } catch (err) {
      setError(err.message || "Nastala chyba.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: "30px", maxWidth: "500px" }}>
      <h2>Test registrácie klienta</h2>

      <form onSubmit={handleSubmit} autoComplete="off">
        <input
          type="text"
          name="firstName"
          placeholder="Meno"
          value={form.firstName}
          onChange={handleChange}
        />
        <br />
        <br />

        <input
          type="text"
          name="lastName"
          placeholder="Priezvisko"
          value={form.lastName}
          onChange={handleChange}
        />
        <br />
        <br />

        <input
          type="date"
          name="dateOfBirth"
          value={form.dateOfBirth}
          onChange={handleChange}
        />
        <br />
        <br />

        <input
          type="text"
          name="phoneNumber"
          placeholder="Telefón"
          value={form.phoneNumber}
          onChange={handleChange}
        />
        <br />
        <br />

        <input
          type="text"
          name="address"
          placeholder="Adresa"
          value={form.address}
          onChange={handleChange}
        />
        <br />
        <br />

        <input
          type="email"
          name="email"
          autoComplete="off"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
        <br />
        <br />

        <div>
          <label>Bezpečnostná otázka </label>
          <select
            name="securityQuestion"
            value={form.securityQuestion}
            onChange={handleChange}
          >
            <option value="">-- Vyber otázku --</option>
            <option value="PET_NAME">
              Ako sa volal tvoj prvý domáci miláčik?
            </option>
            <option value="BIRTH_CITY">V akom meste si sa narodil?</option>
            <option value="CHILDHOOD_NICKNAME">
              Ako sa volala tvoja detská prezývka?
            </option>
            <option value="FAVORITE_TEACHER">Meno obľúbeného učiteľa?</option>
            <option value="STREET_GROWING_UP">
              Názov ulice kde ste vyrastali?
            </option>
            <option value="FIRST_SCHOOL">
              Ako sa volala tvoja prvá škola?
            </option>
            <option value="MOTHER_MAIDEN_NAME">
              Aké bolo rodné priezvisko tvojej mamy?
            </option>
            <option value="FAVORITE_SINGER_CHILDHOOD">
              Kto bol tvoj obľúbený spevák v detstve?
            </option>
            <option value="FIRST_CAR">
              Aká bola značka tvojho prvého auta?
            </option>
            <option value="DREAM_JOB_CHILDHOOD">
              Aké bolo tvoje vysnívané povolanie v detstve?
            </option>
          </select>
        </div>

        <div>
          <label>Bezpečnostná odpoveď </label>
          <input
            type="password"
            name="securityAnswer"
            autoComplete="new-password"
            value={form.securityAnswer}
            onChange={handleChange}
          />
        </div>

        <div>
          <label>Potvrdenie bezpečnostnej odpovede </label>
          <input
            type="password"
            name="confirmSecurityAnswer"
            autoComplete="new-password"
            value={form.confirmSecurityAnswer}
            onChange={handleChange}
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Odosiela sa..." : "Registrovať klienta"}
        </button>
      </form>

      {error && (
        <div style={{ marginTop: "20px", color: "red" }}>
          <strong>Chyba:</strong> {error}
        </div>
      )}

      {result && (
        <div style={{ marginTop: "20px" }}>
          <h3>Výsledok:</h3>
          <p>
            <strong>Client ID:</strong> {result.clientId}
          </p>
          <p>
            <strong>User ID:</strong> {result.userId}
          </p>
          <p>
            <strong>Email:</strong> {result.email}
          </p>
          <p>
            <strong>Rola:</strong> {result.role}
          </p>
          <p>
            <strong>Dočasné heslo:</strong> {result.temporaryPassword}
          </p>
          <p>
            <strong>Správa:</strong> {result.message}
          </p>
        </div>
      )}
    </div>
  );
}
