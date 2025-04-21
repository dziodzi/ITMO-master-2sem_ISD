import { makeAutoObservable } from "mobx";

class ValidationStore {
  file: File | null = null;
  result: "real" | "fake" | null = null;
  probability: number | null = null;
  fileName: string = "";
  loading = false;

  constructor() {
    makeAutoObservable(this);
  }

  async validate(file: File) {
    this.loading = true;
    this.file = file;

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("/validate", {
        method: "POST",
        body: formData,
      });

      const data = await response.json();
      this.result = data.result;
      this.probability = data.probability;
      this.fileName = data.fileName;
    } catch (e) {
      console.error("Validation failed", e);
    } finally {
      this.loading = false;
    }
  }

  reset() {
    this.file = null;
    this.result = null;
    this.probability = null;
    this.fileName = "";
  }
}

export const validationStore = new ValidationStore();
