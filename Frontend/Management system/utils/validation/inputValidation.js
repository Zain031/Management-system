export const validateEmail = (email) => {
  const re =
    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
};

export const validatePassword = (password) => {
  const re =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  return re.test(String(password));
};

export const validateName = (name) => {
  const re = /^[a-zA-Z0-9 ]*$/;
  return re.test(String(name));
};

export const validatePhoneNumber = (phone) => {
  const re = /^\d{10}$/;
  return re.test(String(phone));
};

export const isValidPositiveNumber = (number) => {
  const re = /^[0-9]+$/;
  return re.test(String(number));
};

export const isPositiveNumber = (value) => {
  if (value.length === 0) {
    return true;
  }
  const regex = /^[0-9]+$/;
  return regex.test(value) && Number(value) > 0;
};
export const isPositiveNumberWithMinValue = (value, min) => {
  if (value.length === 0) {
    return true;
  }
  const regex = /^[0-9]+$/;
  return regex.test(value) && Number(value) >= min;
};
export const isEmailValid = (email) => {
  if (email.length === 0) {
    return true;
  }
  const re = /^\S+@\S+\.\S+$/;
  return re.test(email);
};

export const isStrongPassword = (password) => {
  if (password.length === 0) {
    return true;
  }
  const re =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  return re.test(password);
};
export const validateImageType = (file) => {
  const allowedTypes = ["image/jpeg", "image/png", "image/gif"];
  return allowedTypes.includes(file.type);
};

export const isNikValid = (nik) => {
  if (nik.length === 0) {
    return true;
  }
  const re = /^[0-9]{16}$/;
  return re.test(nik);
};
