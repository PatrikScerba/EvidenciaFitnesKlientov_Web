import { useState } from "react";

export default function ClientRegister() {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    dateOfBirth: "",
    phoneNumber: "",
    address: "",
    email: "",
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

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="firstName"
          placeholder="Meno"
          value={form.firstName}
          onChange={handleChange}
        />
        <br /><br />

        <input
          type="text"
          name="lastName"
          placeholder="Priezvisko"
          value={form.lastName}
          onChange={handleChange}
        />
        <br /><br />

        <input
          type="date"
          name="dateOfBirth"
          value={form.dateOfBirth}
          onChange={handleChange}
        />
        <br /><br />

        <input
          type="text"
          name="phoneNumber"
          placeholder="Telefón"
          value={form.phoneNumber}
          onChange={handleChange}
        />
        <br /><br />

        <input
          type="text"
          name="address"
          placeholder="Adresa"
          value={form.address}
          onChange={handleChange}
        />
        <br /><br />

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
        <br /><br />

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
          <p><strong>Client ID:</strong> {result.clientId}</p>
          <p><strong>User ID:</strong> {result.userId}</p>
          <p><strong>Email:</strong> {result.email}</p>
          <p><strong>Rola:</strong> {result.role}</p>
          <p><strong>Dočasné heslo:</strong> {result.temporaryPassword}</p>
          <p><strong>Správa:</strong> {result.message}</p>
        </div>
      )}
    </div>
  );
}