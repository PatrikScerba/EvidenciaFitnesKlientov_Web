import useEmailForm from "../../hooks/useEmailForm";
import { useEffect, useRef, useState } from "react";
import { getAllClients } from "../../api/clientApi";

export default function SendEmailModal({
  client,
  onClose,
  onSuccess,
  mode = "single",
  sendToAll = false,
  selectedClientIds = [],
  selectedClients = [],
  onSendingChange,
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

  const [allRecipients, setAllRecipients] = useState([]);
  const [recipientsLoading, setRecipientsLoading] = useState(false);
  const [recipientsError, setRecipientsError] = useState("");
  const [showAllRecipients, setShowAllRecipients] = useState(false);
  const [confirmation, setConfirmation] = useState(null);

  const sendingRef = useRef(false);

  const effectiveMode =
    mode === "selected"
      ? "selected"
      : mode === "all" || sendToAll
        ? "all"
        : "single";

  const isSelectedMode = effectiveMode === "selected";
  const isAllMode = effectiveMode === "all";
  const isSingleMode = effectiveMode === "single";

  useEffect(() => {
    if (!isAllMode) return;

    let cancelled = false;

    async function fetchRecipients() {
      setRecipientsLoading(true);
      setRecipientsError("");

      try {
        const response = await getAllClients();

        const clients = Array.isArray(response) ? response : response?.content;

        if (!Array.isArray(clients)) {
          throw new Error("Neplatný formát zoznamu klientov.");
        }

        if (!cancelled) {
          setAllRecipients(clients);
        }
      } catch (err) {
        if (!cancelled) {
          setRecipientsError(
            err.message || "Nepodarilo sa načítať zoznam klientov."
          );
        }
      } finally {
        if (!cancelled) {
          setRecipientsLoading(false);
        }
      }
    }

    fetchRecipients();

    return () => {
      cancelled = true;
    };
  }, [isAllMode]);

  const sendEmailOnce = async (clientIds, sendToAll) => {
    if (sendingRef.current) return;

    sendingRef.current = true;
    onSendingChange?.(true);

    try {
      const success = await handleSubmit(clientIds, sendToAll);

      if (success) {
        onSuccess?.();
      }
    } finally {
      onSendingChange?.(false);
      sendingRef.current = false;
    }
  };

  const onSubmit = async (event) => {
    event.preventDefault();

    if (isSending || sendingRef.current || confirmation) {
      return;
    }

    if (isSelectedMode && selectedClientIds.length === 0) {
      return;
    }

    if (
      isAllMode &&
      (recipientsLoading || recipientsError || allRecipients.length === 0)
    ) {
      return;
    }

    if (isSingleMode && !client?.clientId) {
      return;
    }

    const clientIds = isAllMode
      ? []
      : isSelectedMode
        ? [...selectedClientIds]
        : client.clientId;

    if (isSelectedMode || isAllMode) {
      setConfirmation({
        clientIds,
        sendToAll: isAllMode,
        count: isAllMode ? allRecipients.length : selectedClientIds.length,
      });

      return;
    }

    await sendEmailOnce(clientIds, false);
  };

  const handleConfirmSend = async () => {
    if (!confirmation || isSending || sendingRef.current) {
      return;
    }

    const { clientIds, sendToAll } = confirmation;

    setConfirmation(null);

    await sendEmailOnce(clientIds, sendToAll);
  };

  const handleCancelConfirmation = () => {
    if (isSending || sendingRef.current) return;

    setConfirmation(null);
  };

  return (
    <div
      style={{
        padding: "20px",
        border: "1px solid #ccc",
      }}
    >
      <h3>Odoslať email klientovi</h3>

      {isAllMode ? (
        <div>
          <p>Príjemcovia: Všetci klienti</p>

          {recipientsLoading && <p>Načítavam zoznam klientov...</p>}

          {recipientsError && <p style={{ color: "red" }}>{recipientsError}</p>}

          {!recipientsLoading && !recipientsError && (
            <div>
              <p>Počet evidovaných klientov: {allRecipients.length}</p>

              {allRecipients.length > 0 && (
                <button
                  type="button"
                  onClick={() => setShowAllRecipients((prev) => !prev)}
                >
                  {showAllRecipients
                    ? "Skryť zoznam príjemcov"
                    : "Zobraziť zoznam príjemcov"}
                </button>
              )}

              {showAllRecipients && allRecipients.length > 0 && (
                <ul
                  style={{
                    maxHeight: "250px",
                    overflowY: "auto",
                  }}
                >
                  {allRecipients.map((recipient) => (
                    <li key={recipient.clientId}>
                      {recipient.firstName} {recipient.lastName} (
                      {recipient.email})
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>
      ) : isSelectedMode ? (
        <div>
          <p>Príjemcovia: {selectedClientIds.length} vybraných klientov</p>

          <ul>
            {selectedClients.map((recipient) => (
              <li key={recipient.clientId}>
                {recipient.firstName} {recipient.lastName} ({recipient.email})
              </li>
            ))}
          </ul>
        </div>
      ) : (
        <p>
          Príjemca: {client?.firstName} {client?.lastName} ({client?.email})
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

        <button
          type="submit"
          disabled={
            isSending ||
            confirmation !== null ||
            (isSingleMode && !client?.clientId) ||
            (isSelectedMode && selectedClientIds.length === 0) ||
            (isAllMode &&
              (recipientsLoading ||
                !!recipientsError ||
                allRecipients.length === 0))
          }
        >
          {isSending ? "Odosielam..." : "Odoslať email"}
        </button>

        <button
          type="button"
          onClick={onClose}
          disabled={isSending || confirmation !== null}
        >
          Zrušiť
        </button>
      </form>

      {confirmation && (
        <div
          style={{
            position: "fixed",
            inset: 0,
            backgroundColor: "rgba(0, 0, 0, 0.7)",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            zIndex: 9999,
          }}
        >
          <div
            role="dialog"
            aria-modal="true"
            aria-labelledby="confirm-email-title"
            style={{
              backgroundColor: "#252525",
              color: "#fff",
              padding: "25px",
              border: "1px solid #666",
              borderRadius: "8px",
              width: "90%",
              maxWidth: "450px",
              boxShadow: "0 8px 30px rgba(0,0,0,0.4)",
            }}
          >
            <h3 id="confirm-email-title">Potvrdenie odoslania e-mailu</h3>

            {confirmation.sendToAll ? (
              <>
                <p>Naozaj chcete odoslať e-mail všetkým klientom?</p>

                <p>
                  Počet evidovaných klientov v náhľade:{" "}
                  <strong>{confirmation.count}</strong>
                </p>
              </>
            ) : (
              <p>
                Naozaj chcete odoslať e-mail{" "}
                <strong>{confirmation.count}</strong> vybraným klientom?
              </p>
            )}

            <div
              style={{
                display: "flex",
                justifyContent: "flex-end",
                gap: "10px",
                marginTop: "25px",
              }}
            >
              <button
                type="button"
                onClick={handleCancelConfirmation}
                disabled={isSending}
              >
                Zrušiť
              </button>

              <button
                type="button"
                onClick={handleConfirmSend}
                disabled={isSending}
              >
                Potvrdiť odoslanie
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
