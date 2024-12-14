import SelectMonth from "../SelectMonth";
import SelectYear from "../SelectYear";
import ButtonExport from "../ButtonExport";
import ButtonAdd from "../buttons/ButtonAdd";

const HeaderAction = ({
  selectedMonth,
  setSelectedMonth,
  selectedYear,
  setSelectedYear,
  selectedStatus,
  setSelectedStatus,
  onButtonExportClick,
  onButtonAddClick,
}) => {
  return (
    <div className="flex justify-end gap-5">
      <label className="form-control max-w-xs">
        <select
          className="select select-bordered w-full my-2"
          defaultValue={selectedStatus}
          placeholder="Select Status"
          onChange={(e) => setSelectedStatus(e.target.value)}>
          <option value="">All Status</option>
          <option value="COMPLETED">Completed</option>
          <option value="PENDING">Pending</option>
          <option value="CANCELED">Canceled</option>
        </select>
      </label>
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
        Export Order
      </ButtonExport>
      <ButtonAdd onPress={onButtonAddClick} dataTip="Product" />
    </div>
  );
};

export default HeaderAction;
