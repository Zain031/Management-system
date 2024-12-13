import { SquarePlus } from "lucide-react";

const ButtonAdd = ({ onPress, dataTip, ...props }) => {
  return (
    <button
      type="button"
      className="tooltip"
      data-tip={`Add ${dataTip}`}
      onClick={onPress}
      {...props}>
      <SquarePlus size={50} color="#00d12a" />
    </button>
  );
};

export default ButtonAdd;
