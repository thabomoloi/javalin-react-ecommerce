export interface ErrorDTO {
  code: number;
  message: string;
  errors?: object | null | undefined;
}

export interface ValidationErrorDTO extends ErrorDTO {
  errors: {
    property: string;
    value: string;
    message: string;
  }[];
}
