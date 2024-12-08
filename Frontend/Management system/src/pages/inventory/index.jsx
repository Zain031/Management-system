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
  fetchInventoryByName,
  setPage,
} from "../../redux/feature/InventorySlice";
import { Pagination } from "@nextui-org/pagination";

const Inventory = () => {
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [category, setCategory] = useState("");
  const [quantity, setQuantity] = useState("");
  const [discount, setDiscount] = useState("");
  const [date, setDate] = useState("");
  const [searchByName, setSearchByName] = useState("");

  const [isEditing, setIsEditing] = useState(false);
  const { inventories, paging, page } = useSelector(
    (state) => state.inventories
  );
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchInventories());
  }, []);

  useEffect(() => {
    if (searchByName === "") {
      dispatch(fetchInventories({ page }));
      return;
    }
    dispatch(fetchInventoryByName({ name: searchByName, page }));
  }, [searchByName]);

  useEffect(() => {
    if (selectedCategory === "") {
      dispatch(fetchInventories({ page }));
      return;
    }
    dispatch(fetchInventoryByCategory({ category: selectedCategory, page }));
  }, [selectedCategory]);

  useEffect(() => {
    console.log("Page", page);
    dispatch(fetchInventories({ page }));
  }, [page]);

  console.log("Paging", paging);

  const resetForm = () => {
    setName("");
    setPrice("");
    setCategory("");
    setQuantity("");
    setDiscount("");
    setDate("");
  };

  // Filter data berdasarkan kategori yang dipilih
  const filterByCategory = (category) => {
    setSelectedCategory(category); // Set kategori yang dipilih
  };

  // Mengambil produk yang sudah difilter berdasarkan kategori
  //   const filteredInventories = inventories?.content?.filter(
  //     (Material) =>
  //       selectedCategory === "" || Material.material_category === selectedCategory
  //   );

  // Handle delete action
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
  const handleEdit = (data) => {
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
      title: "Material has been updated",
    });
    document.getElementById("form_modal").close();
    resetForm();
    dispatch(fetchInventories());
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    document.getElementById("form_modal").showModal();
    const data = {
      id_user: 2,
      material_name: name,
      material_category: category,
      material_price_unit: price,
      material_quantity: quantity,
      material_discount: discount,
      date_material_buy: date,
    };

    if (isEditing) {
      handleEdit(data);
    } else {
      handleAdd(data);
    }
  };

  const onButtonAddClick = () => {
    document.getElementById("form_modal").showModal();
  };

  return (
    <>
      <Container>
        <Header title="Inventory" />
        <div className="flex justify-end gap-5">
          <input
            type="text"
            placeholder="Search"
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

          <button
            onClick={onButtonAddClick}
            className="tooltip"
            data-tip="Add Material">
            <SquarePlus size={50} color="#00d12a" />
          </button>
        </div>

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

              <input
                type="text"
                placeholder="Material Price"
                className="input input-bordered input-ghost w-full my-2"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
              />

              <input
                type="text"
                placeholder="Material Quantity"
                className="input input-bordered input-ghost w-full my-2"
                value={quantity}
                onChange={(e) => setQuantity(e.target.value)}
              />

              <input
                type="text"
                placeholder="Material Discount"
                className="input input-bordered input-ghost w-full my-2"
                value={discount}
                onChange={(e) => setDiscount(e.target.value)}
              />

              <select
                value={category}
                className="input input-bordered input-ghost w-full my-2"
                onChange={(e) => setCategory(e.target.value)}>
                <option value="FOODSTUFF">Foods Stuff</option>
                <option value="TOOL">Toll</option>
                <option value="ETC">Others</option>
              </select>

              <input
                type="date"
                className="input input-bordered input-ghost w-full my-2"
                value={date}
                onChange={(e) => setDate(e.target.value)}
              />

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
                            ? "bg-red-600 w-14 px-2 py-1 text-white rounded-sm"
                            : item.material_category === "TOOL"
                            ? "bg-blue-600 w-14 px-2 py-1 text-white rounded-sm"
                            : item.material_category === "ETC"
                            ? "bg-green-600 w-14 px-2 py-1 text-white rounded-sm"
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
                        (item.material_price_discount /
                          (item.material_total_price +
                            item.material_price_discount)) *
                          100
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

                    <td className="flex gap-8">
                      <button className="tooltip" data-tip="Edit">
                        <SquarePen size={28} color="#00d15b" />
                      </button>
                      <button
                        onClick={() => handleDelete(item.id_Material)}
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
        <div className="my-6">
          <Pagination
            initialPage={page}
            total={paging?.totalPages}
            onChange={(page) => dispatch(setPage(page))}
          />
        </div>
      </Container>
    </>
  );
};

export default Inventory;
