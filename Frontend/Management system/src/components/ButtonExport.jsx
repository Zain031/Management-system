import { Download } from "lucide-react";

const ButtonExport = ({ children, onPress, ...props }) => {
  return (
    <button
      className="tooltip"
      onClick={onPress}
      {...props}
      data-tip={children}>
      <Download size={40} color="#00d12a" />
    </button>
  );
};

export default ButtonExport;
