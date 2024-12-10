export const isEmailValid = (email) => {
  const re =
    /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
  return re.test(String(email).toLowerCase());
};

export const isStrongPassword = (password) => {
  const re =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
  return re.test(password);
};

export const validateName = (name) => {
  const re = /^[a-zA-Z0-9 ]*$/;
  return re.test(String(name));
};

export const validatePhoneNumber = (phone) => {
  const re = /^\d{10}$/;
  return re.test(String(phone));
};

export const isBoolean = (value) => {
  if (value === true || value === false) {
    return true;
  }
  return false;
};

export const isNotEmpty = (value) => {
  if (!value) {
    return false;
  }
  if (!value.trim()) {
    return false;
  }
  return true;
};

export const isValidPositiveNumber = (number) => {
  const re = /^[0-9]+$/;
  return re.test(String(number));
};

export const isPositiveNumberWithMinValue = (value, min) => {
  if (value.length === 0) {
    return true;
  }
  const regex = /^[0-9]+$/;
  return regex.test(value) && Number(value) >= min;
};

export const isNikValid = (nik) => {
  if (nik.length === 0) {
    return true;
  }
  const re = /^[0-9]{16}$/;
  return re.test(nik);
};

export const validateImageType = (file) => {
  const allowedTypes = ["image/jpeg", "image/png", "image/gif"];
  return allowedTypes.includes(file.type);
};

export const dynamicValidation = (validation, data) => {
  let allValid = true;
  for (let i = 0; i < validation.length; i++) {
    if (!validation[i].validator(data[validation[i].name])) {
      validation[i].negativeImpact();
      allValid = false;
    } else {
      validation[i].positiveImpact();
    }
  }
  return allValid;
};
