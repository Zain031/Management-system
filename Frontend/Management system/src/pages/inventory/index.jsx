import { useEffect, useState } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
import { Trash2 } from "lucide-react";
import { SquarePen, SquarePlus } from "lucide-react";
import {
  createInventory,
  deleteInventory,
  fetchInventories,
  fetchInventoryByCategory,
  fetchInventoryById,
  fetchInventoryByName,
  setPage,
  updateInventory,
} from "../../redux/feature/InventorySlice";
import { Pagination } from "@nextui-org/pagination";
import ButtonExport from "../../components/ButtonExport";
import {
  exportInventoryPerDate,
  exportInventoryPerMonth,
} from "../../redux/feature/exportSlice";
import {
  dynamicValidation,
  isNotEmpty,
  isPersentaseValid,
  isValidPositiveNumber,
  validateName,
} from "../../../utils/validation/inputValidation";
import yearOptionRange from "../../../utils/yearOptionRange";

const Inventory = () => {
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [category, setCategory] = useState("");
  const [quantity, setQuantity] = useState("");
  const [discount, setDiscount] = useState("");
  const [date, setDate] = useState("");
  const [searchByName, setSearchByName] = useState("");
  const [exportPer, setExportPer] = useState("");
  const [selectedYear, setSelectedYear] = useState("");
  const [selectedMonth, setSelectedMonth] = useState("");
  const [selectedDate, setSelectedDate] = useState("");

  const { user } = useSelector((state) => state.auth);

  const [materialNameValid, setMaterialNameValid] = useState({
    valid: true,
    message: "",
  });
  const [materialPriceValid, setMaterialPriceValid] = useState({
    valid: true,
    message: "",
  });
  const [materialCategoryValid, setMaterialCategoryValid] = useState({
    valid: true,
    message: "",
  });
  const [materialQuantityValid, setMaterialQuantityValid] = useState({
    valid: true,
    message: "",
  });
  const [materialDiscountValid, setMaterialDiscountValid] = useState({
    valid: true,
    message: "",
  });
  const [materialDateValid, setMaterialDateValid] = useState({
    valid: true,
    message: "",
  });

  const [isEditing, setIsEditing] = useState(false);
  const { inventories, paging, page, inventoryById } = useSelector(
    (state) => state.inventories
  );
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchInventories());
  }, [dispatch]);

  useEffect(() => {
    dispatch(setPage(1));
    if (searchByName === "") {
      dispatch(fetchInventories({ page }));
      return;
    }
    dispatch(fetchInventoryByName({ name: searchByName, page: 1 }));
  }, [searchByName, dispatch]);

  useEffect(() => {
    dispatch(setPage(1));
    if (selectedCategory === "") {
      dispatch(fetchInventories({ page }));
      return;
    }
    dispatch(fetchInventoryByCategory({ category: selectedCategory, page: 1 }));
  }, [selectedCategory]);

  useEffect(() => {
    dispatch(fetchInventories({ page }));
  }, [page]);

  useEffect(() => {
    if (isEditing) {
      setName(inventoryById.material_name);
      setPrice(inventoryById.material_price_unit);
      setCategory(inventoryById.material_category);
      setQuantity(inventoryById.material_quantity);
      setDiscount(inventoryById.material_discount);
      setDate(inventoryById.date_material_buy);
    }
  }, [inventoryById]);

  const resetForm = () => {
    setName("");
    setPrice("");
    setCategory("");
    setQuantity("");
    setDiscount("");
    setDate("");
  };

  const filterByCategory = (category) => {
    setSelectedCategory(category);
  };

  const handleDelete = async (id) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    });

    if (result.isConfirmed) {
      try {
        await dispatch(deleteInventory(id)).unwrap();
        const Toast = Swal.mixin({
          toast: true,
          position: "top-end",
          showConfirmButton: false,
          timer: 1500,
          timerProgressBar: true,
          didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
          },
        });
        Toast.fire({
          icon: "success",
          title: "Material has been deleted",
        });
        await dispatch(fetchInventories());
      } catch (error) {
        console.log(error);
      }
    }
  };

  const handlePageChange = (newPage) => {
    dispatch(setPage(newPage));
  };

  const handleAdd = async (data) => {
    await dispatch(createInventory(data)).unwrap();
    const Toast = Swal.mixin({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 1500,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.onmouseenter = Swal.stopTimer;
        toast.onmouseleave = Swal.resumeTimer;
      },
    });
    Toast.fire({
      icon: "success",
      title: "Material has been added",
    });

    document.getElementById("form_modal").close();
    resetForm();
    await dispatch(fetchInventories());
  };
  const handleEdit = async (data) => {
    const Toast = Swal.mixin({
      toast: true,
      position: "top-end",
      showConfirmButton: false,
      timer: 1500,
      timerProgressBar: true,
      didOpen: (toast) => {
        toast.onmouseenter = Swal.stopTimer;
        toast.onmouseleave = Swal.resumeTimer;
      },
    });
    await dispatch(updateInventory({ id: inventoryById.id_material, data }));
    Toast.fire({
      icon: "success",
      title: "Material has been updated",
    });
    document.getElementById("form_modal").close();
    resetForm();
    await dispatch(fetchInventories());
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const data = {
      id_user: user.id_user,
      material_name: name,
      material_category: category,
      material_price_unit: price,
      material_quantity: quantity,
      material_discount: discount,
      date_material_buy: date,
    };

    const validation = [
      {
        name: "material_name",
        validator: (name) => {
          return isNotEmpty(name) && validateName(name);
        },
        negativeImpact: () =>
          setMaterialNameValid({
            valid: false,
            message:
              "Please enter a valid name, only letters and numbers are allowed",
          }),
        positiveImpact: () =>
          setMaterialNameValid({
            valid: true,
            message: "",
          }),
      },
      {
        name: "material_category",
        validator: (categories) => {
          return isNotEmpty(categories);
        },
        negativeImpact: () =>
          setMaterialCategoryValid({
            valid: false,
            message: "Please select a valid category",
          }),
        positiveImpact: () =>
          setMaterialCategoryValid({
            valid: true,
            message: "",
          }),
      },
      {
        name: "material_price_unit",
        validator: (price) => {
          return isNotEmpty(price) && isValidPositiveNumber(price);
        },
        negativeImpact: () =>
          setMaterialPriceValid({
            valid: false,
            message:
              "Please enter a valid price, only positive numbers are allowed",
          }),
        positiveImpact: () =>
          setMaterialPriceValid({
            valid: true,
            message: "",
          }),
      },
      {
        name: "material_quantity",
        validator: (quantity) => {
          return isNotEmpty(quantity) && isValidPositiveNumber(quantity);
        },
        negativeImpact: () =>
          setMaterialQuantityValid({
            valid: false,
            message:
              "Please enter a valid quantity, only positive numbers are allowed",
          }),
        positiveImpact: () =>
          setMaterialQuantityValid({
            valid: true,
            message: "",
          }),
      },
      {
        name: "material_discount",
        validator: (discount) => {
          const number = Number(discount);
          return isNotEmpty(discount) && isValidPositiveNumber(discount) &&
            (number <= 100 || isPersentaseValid(discount));
        },
        negativeImpact: () =>
          setMaterialDiscountValid({
            valid: false,
            message:
              "Please enter a valid discount, only positive numbers are allowed and not more than 100%",
          }),
        positiveImpact: () =>
          setMaterialDiscountValid({
            valid: true,
            message: "",
          }),
      },
      {
        name: "date_material_buy",
        validator: (date) => {
          return isNotEmpty(date);
        },
        negativeImpact: () =>
          setMaterialDateValid({
            valid: false,
            message: "Please enter a valid date",
          }),
        positiveImpact: () =>
          setMaterialDateValid({
            valid: true,
            message: "",
          }),
      },
    ];

    let allValid = dynamicValidation(validation, data);
    if (!allValid) {
      return;
    }

    document.getElementById("form_modal").showModal();
    if (isEditing) {
      handleEdit(data);
    } else {
      handleAdd(data);
    }
  };

  const onButtonAddClick = () => {
    document.getElementById("form_modal").showModal();
  };

  const onButtonEditClick = async (id) => {
    setIsEditing(true);
    await dispatch(fetchInventoryById(id));
    document.getElementById("form_modal").showModal();
  };

  const onButtonDeleteClick = (id) => {
    handleDelete(id);
  };

  const onButtonExportClick = () => {
    document.getElementById("export_modal").showModal();
  };

  const handleExport = (e) => {
    e.preventDefault();
    if (exportPer == "date") {
      dispatch(exportInventoryPerDate({ date: selectedDate }));
    } else {
      dispatch(
        exportInventoryPerMonth({ month: selectedMonth, year: selectedYear })
      );
    }
  };

  return (
    <>
      <Container>
        <Header title="Inventory" />
        <div className="flex justify-end gap-5">
          <input
            type="text"
            placeholder="Search by name"
            onChange={(e) => setSearchByName(e.target.value)}
            className="input input-bordered w-full max-w-xs"
          />

          <label className="form-control max-w-xs">
            <select
              className="select select-bordered"
              value={selectedCategory}
              onChange={(e) => filterByCategory(e.target.value)}>
              <option value="">All Categories</option>
              <option value="FOODSTUFF">Foodstuffs</option>
              <option value="TOOL">Tools</option>
              <option value="ETC">Others</option>
            </select>
          </label>

          <ButtonExport onPress={onButtonExportClick}>
            Export Inventory
          </ButtonExport>

          <button
            onClick={onButtonAddClick}
            className="tooltip"
            data-tip="Add Material">
            <SquarePlus size={50} color="#00d12a" />
          </button>
        </div>

        <dialog id="export_modal" className="modal">
          <div className="modal-box">
            <form onSubmit={handleExport}>
              <select
                className="select select-bordered w-full my-2"
                value={exportPer}
                onChange={(e) => setExportPer(e.target.value)}>
                <option value="" disabled>
                  Select Export Per Date Or Month
                </option>
                <option value="month">Month</option>
                <option value="date">Date</option>
              </select>
              {exportPer === "month" && (
                <>
                  <select
                    className="select select-bordered w-full my-2"
                    defaultValue={selectedMonth}
                    placeholder="Select Month"
                    onChange={(e) => setSelectedMonth(e.target.value)}>
                    <option value="" disabled>
                      Select Month
                    </option>
                    Select Month
                    <option value="january">January</option>
                    <option value="february">February</option>
                    <option value="march">March</option>
                    <option value="april">April</option>
                    <option value="may">May</option>
                    <option value="june">June</option>
                    <option value="july">July</option>
                    <option value="august">August</option>
                    <option value="september">September</option>
                    <option value="october">October</option>
                    <option value="november">November</option>
                    <option value="december">December</option>
                  </select>
                  <select
                    className="select select-bordered w-full my-2"
                    defaultValue={selectedYear}
                    placeholder="Select Year"
                    onChange={(e) => setSelectedYear(e.target.value)}>
                    <option value="" disabled>
                      Select Year
                    </option>
                    {yearOptionRange().map((year) => (
                      <option key={year} value={year}>
                        {year}
                      </option>
                    ))}
                  </select>
                </>
              )}
              {exportPer === "date" && (
                <input
                  type="date"
                  className="input input-bordered input-ghost w-full my-2"
                  value={selectedDate}
                  onChange={(e) =>
                    setSelectedDate(e.target.value.split("T")[0])
                  }
                />
              )}
              <button className="btn btn-outline btn-primary w-full">
                Submit
              </button>
            </form>
            <div className="modal-action">
              <form method="dialog">
                <button className="btn">Close</button>
              </form>
            </div>
          </div>
        </dialog>

        <dialog id="form_modal" className="modal">
          <div className="modal-box">
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                placeholder="Material Name"
                className="input input-bordered input-ghost w-full my-2"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              {!materialNameValid.valid && (
                <p className="text-red-500">{materialNameValid.message}</p>
              )}

              <input
                type="number"
                placeholder="Material Price"
                className="input input-bordered input-ghost w-full my-2"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
              />
              {!materialPriceValid.valid && (
                <p className="text-red-500">{materialPriceValid.message}</p>
              )}

              <input
                type="number"
                placeholder="Material Quantity"
                className="input input-bordered input-ghost w-full my-2"
                value={quantity}
                onChange={(e) => setQuantity(e.target.value)}
              />
              {!materialQuantityValid.valid && (
                <p className="text-red-500">{materialQuantityValid.message}</p>
              )}

              <input
                type="number"
                placeholder="Material Discount"
                className="input input-bordered input-ghost w-full my-2"
                value={discount}
                onChange={(e) => setDiscount(e.target.value)}
              />
              {!materialDiscountValid.valid && (
                <p className="text-red-500">{materialDiscountValid.message}</p>
              )}

              <select
                value={category}
                className="input input-bordered input-ghost w-full my-2"
                onChange={(e) => setCategory(e.target.value)}>
                <option value="">Select Category</option>
                <option value="FOODSTUFF">Foods Stuff</option>
                <option value="TOOL">Tool</option>
                <option value="ETC">Others</option>
              </select>
              {!materialCategoryValid.valid && (
                <p className="text-red-500">{materialCategoryValid.message}</p>
              )}

              <input
                type="date"
                className="input input-bordered input-ghost w-full my-2"
                defaultValue={isEditing ? date : ""}
                onChange={(e) => setDate(e.target.value)}
              />
              {!materialDateValid.valid && (
                <p className="text-red-500">{materialDateValid.message}</p>
              )}

              <button className="btn btn-outline btn-primary w-full">
                Submit
              </button>
            </form>
            <div className="modal-action">
              <form method="dialog">
                <button
                  className="btn"
                  onClick={() => {
                    setIsEditing(false);
                    resetForm();
                  }}>
                  Close
                </button>
              </form>
            </div>
          </div>
        </dialog>

        <div className="overflow-x-auto shadow-lg outline outline-1 outline-slate-300 rounded-md mt-2">
          <table className="table">
            <thead>
              <tr>
                <th></th>
                <th>Name</th>
                <th>Category</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Discount (per Material)</th>
                <th>Total Price</th>
                <th>User name</th>
                <th>Date Buy</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(inventories) && inventories.length > 0 ? (
                inventories.map((item, index) => (
                  <tr key={item.id_material}>
                    <th>{(page - 1) * 10 + ++index}</th>
                    <td>{item.material_name}</td>
                    <td>
                      <p
                        className={` ${
                          item.material_category === "FOODSTUFF"
                            ? "bg-red-600 w-14 px-2 py-1 text-white rounded-md text-center"
                            : item.material_category === "TOOL"
                            ? "bg-blue-600 w-14 px-2 py-1 text-white rounded-md text-center"
                            : item.material_category === "ETC"
                            ? "bg-green-600 w-14 px-2 py-1 text-white rounded-md text-center"
                            : ""
                        } font-bold`}>
                        {item.material_category === "FOODSTUFF"
                          ? "Food"
                          : item.material_category === "TOOL"
                          ? "Tool"
                          : item.material_category === "ETC"
                          ? "Other"
                          : item.material_category}
                      </p>
                    </td>

                    <td>
                      {new Intl.NumberFormat("id-ID", {
                        style: "currency",
                        currency: "IDR",
                      }).format(item.material_price_unit)}
                    </td>

                    <td>{item.material_quantity}</td>

                    <td>
                      {new Intl.NumberFormat("id-ID").format(
                        item.material_discount
                      )}{" "}
                      %
                    </td>

                    <td>
                      {new Intl.NumberFormat("id-ID", {
                        style: "currency",
                        currency: "IDR",
                      }).format(item.material_total_price)}
                    </td>

                    <td>{item.user.name}</td>

                    <td>{item.date_material_buy}</td>

                    <td className="flex gap-8">
                      <button
                        className="tooltip"
                        data-tip="Edit"
                        onClick={() => onButtonEditClick(item.id_material)}>
                        <SquarePen size={28} color="#00d15b" />
                      </button>
                      <button
                        onClick={() => onButtonDeleteClick(item.id_material)}
                        className=" text-white tooltip"
                        data-tip="Delete">
                        <Trash2 size={28} color="#d12a00" />
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="text-center">
                    No Materials available
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        {inventories &&
          inventories.length > 0 &&
          paging &&
          paging.totalPages > 1 && (
            <div className="my-6">
              <Pagination
                initialPage={page}
                total={paging?.totalPages}
                onChange={handlePageChange}
              />
            </div>
          )}
      </Container>
    </>
  );
};

export default Inventory;
