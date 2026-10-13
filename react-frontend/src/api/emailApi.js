import { apiFetch } from "./api";

export async function sendEmail(emailData, attachments = []) {
  const formData = new FormData();

  formData.append(
    "request",
    new Blob([JSON.stringify(emailData)], {
      type: "application/json",
    })
  );

  attachments.forEach((attachment) => {
    formData.append("attachments", attachment);
  });

  return apiFetch("/api/emails", {
    method: "POST",
    body: formData,
  });
}
