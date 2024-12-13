import SelectMonth from "../SelectMonth";
import SelectYear from "../SelectYear";
import ButtonExport from "../ButtonExport";
import ButtonAdd from "../buttons/ButtonAdd";

const HeaderAction = ({
  selectedMonth,
  setSelectedMonth,
  selectedYear,
  setSelectedYear,
  onButtonExportClick,
  onButtonAddClick,
}) => {
  return (
    <div className="flex justify-end gap-5">
      <label className="form-control max-w-xs">
        <SelectMonth
          selectedMonth={selectedMonth}
          setSelectedMonth={setSelectedMonth}
        />
      </label>
      <label className="form-control max-w-xs">
        <SelectYear
          selectedYear={selectedYear}
          setSelectedYear={setSelectedYear}
        />
      </label>

      <ButtonExport onPress={() => onButtonExportClick()}>
        Export Products
      </ButtonExport>
      <ButtonAdd onPress={onButtonAddClick} dataTip="Product" />
    </div>
  );
};

export default HeaderAction;
