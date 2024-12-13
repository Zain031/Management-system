import { Eye } from "lucide-react";

const ButtonEye = ({ ...props }) => {
  return (
    <button {...props}>
      <Eye size={28} color="#00d15b" />
    </button>
  );
};

export default ButtonEye;
