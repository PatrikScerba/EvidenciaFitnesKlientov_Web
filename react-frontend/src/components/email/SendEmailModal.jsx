import useEmailForm from "../../hooks/useEmailForm";

export default function SendEmailModal({
  client,
  onClose,
  sendToAll = false,
  selectedClientIds = [],
}) {
  const {
    emailData,
    error,
    isSending,
    handleChange,
    attachmentInputRef,
    handleAttachmentsChange,
    handleSubmit,
  } = useEmailForm();

  const isSelectedMode = selectedClientIds.length > 0;

  const onSubmit = async (event) => {
    event.preventDefault();

    const clientIds = sendToAll
      ? []
      : isSelectedMode
        ? selectedClientIds
        : client.clientId;

    await handleSubmit(clientId, sendToAll);
  };

  return (
    <div style={{ padding: "20px", border: "1px solid #ccc" }}>
      <h3>Odoslať email klientovi</h3>

      {sendToAll ? (
        <p>Príjemcovia: Všetci klienti</p>
      ) : isSelectedMode ? (
        <p>Príjemcovia: {selectedClientIds.length} vybraných klientov</p>
      ) : (
        <p>
          Príjemca: {client.firstName} {client.lastName} ({client.email})
        </p>
      )}

      <form onSubmit={onSubmit}>
        <div>
          <label>Predmet</label>
          <input
            type="text"
            name="subject"
            value={emailData.subject}
            onChange={handleChange}
            maxLength={150}
            required
          />
        </div>

        <div>
          <label>Správa</label>
          <textarea
            name="message"
            value={emailData.message}
            onChange={handleChange}
            maxLength={5000}
            rows={6}
          />
        </div>

        <div>
          <label>Prílohy</label>
          <input
            type="file"
            multiple
            ref={attachmentInputRef}
            onChange={handleAttachmentsChange}
          />
        </div>

        {error && <p style={{ color: "red" }}>{error}</p>}

        <button type="submit" disabled={isSending}>
          {isSending ? "Odosielam..." : "Odoslať email"}
        </button>

        <button type="button" onClick={onClose} disabled={isSending}>
          Zrušiť
        </button>
      </form>
    </div>
  );
}
