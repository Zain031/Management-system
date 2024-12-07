import { useEffect, useState } from "react";
import Container from "../../components/container";
import Header from "../../layouts/partials/header";
import { useDispatch, useSelector } from "react-redux";
import Swal from "sweetalert2";
import {
  createProduct,
  deleteProduct,
  fetchProductById,
  fetchProducts,
  fetchProductsByCategory,
  fetchProductsByName,
  setPage,
  updateProduct,
} from "../../redux/feature/ProductsSlice";
import { Trash2 } from "lucide-react";
import { SquarePen, SquarePlus } from "lucide-react";
import { Pagination } from "@nextui-org/pagination";

const Products = () => {
  const [productId, setProductId] = useState(null);
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("");
  const [categories, setCategories] = useState("");
  const [isStockAvailable, setIsStockAvailable] = useState("");
  const tableHeaders = [
    "Name",
    "Category",
    "Price",
    "User name",
    "Available",
    "Action",
  ];

  const [isEditing, setIsEditing] = useState(false);

  const [searchByName, setSearchByName] = useState("");

  const { products, paging, productById, page } = useSelector(
    (state) => state.products
  );

  useEffect(() => {
    setName(productById?.product_name || "");
    setPrice(productById?.product_price) || "";
    setCategories(productById?.categories || "");
    setIsStockAvailable(productById?.available_stock || false);
  }, [productById]);

  const dispatch = useDispatch();
  useEffect(() => {
    dispatch(fetchProducts());
  }, []);

  useEffect(() => {
    dispatch(fetchProducts({ page: page }));
  }, [page]);

  useEffect(() => {
    if (searchByName === "") {
      dispatch(fetchProducts());
      return;
    }
    dispatch(fetchProductsByName(searchByName));
  }, [searchByName]);

  useEffect(() => {
    if (selectedCategory === "") {
      dispatch(fetchProducts());
    }
    dispatch(fetchProductsByCategory(selectedCategory));
  }, [selectedCategory]);

  const resetForm = () => {
    setName("");
    setPrice("");
    setCategories("");
    setIsStockAvailable("");
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
        await dispatch(deleteProduct(id)).unwrap();

        Swal.fire({
          icon: "success",
          title: "Product has been deleted",
          timer: 1500,
        });

        await dispatch(fetchProducts());
      } catch (error) {
        console.log(error);
      }
    }
  };

  const onButtonEditClick = (id) => {
    setIsEditing(true);
    document.getElementById("modal_form_product").showModal();
    setProductId(id);
    dispatch(fetchProductById(id));
  };
  const handleEdit = async (data) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, update it!",
    });
    if (result.isConfirmed) {
      try {
        await dispatch(updateProduct(data)).unwrap();
        setIsEditing(false);

        Swal.fire({
          icon: "success",
          title: "Product has been updated",
          timer: 1500,
        });
        document.getElementById("modal_form_product").close();
        await dispatch(fetchProducts());
      } catch (error) {
        console.log(error);
      }
    }
  };

  const onButtonAddClick = () => {
    document.getElementById("modal_form_product").showModal();
  };
  const handleAdd = async (data) => {
    await dispatch(createProduct(data));
    Swal.fire({
      icon: "success",
      title: "Product has been added",
      timer: 1500,
    });

    document.getElementById("modal_form_product").close();
    await dispatch(fetchProducts());
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    document.getElementById("modal_form_product").close();
    const data = {
      id_user: 2,
      product_name: name,
      product_price: price,
      categories,
      available_stock: isStockAvailable,
    };
    if (isEditing) {
      handleEdit({ id: productId, ...data });
    } else {
      handleAdd(data);
    }
    resetForm();
  };

  //   const filteredProducts = products?.filter((product) =>
  //     selectedCategory ? product.categories === selectedCategory : true
  //   );

  return (
    <>
      <Container>
        <Header title="Products" />
        <div className="flex justify-end gap-5">
          <input
            type="text"
            placeholder="Search"
            className="input input-bordered w-full max-w-xs"
            onChange={(e) => setSearchByName(e.target.value)}
          />

          <label className="form-control max-w-xs">
            <select
              className="select select-bordered"
              value={isEditing ? productById?.categories : ""}
              onChange={(e) => setSelectedCategory(e.target.value)}>
              <option value="">All Categories</option>
              <option value="FOODS">Foods</option>
              <option value="DRINKS">Drinks</option>
            </select>
          </label>

          <button
            onClick={onButtonAddClick}
            className="tooltip"
            data-tip="Add Product">
            <SquarePlus size={50} color="#00d12a" />
          </button>
        </div>

        <dialog id="modal_form_product" className="modal">
          <div className="modal-box">
            <form onSubmit={handleSubmit}>
              <input
                type="text"
                placeholder="Product Name"
                className="input input-bordered input-ghost w-full my-2"
                value={name}
                onChange={(e) => setName(e.target.value)}
              />

              <input
                type="number"
                placeholder="Product Price"
                className="input input-bordered input-ghost w-full my-2"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
              />

              <select
                className="select select-bordered w-full my-2"
                selected={categories}
                onChange={(e) => setCategories(e.target.value)}>
                <option value="" disabled selected>
                  Select Category
                </option>
                <option value="DRINKS">Drink</option>
                <option value="FOODS">Food</option>
              </select>

              <select
                className="select select-bordered w-full my-2"
                onChange={(e) =>
                  setIsStockAvailable(e.target.value === "true")
                }>
                <option value="" disabled selected>
                  Available Stock
                </option>
                <option value="true">Ready</option>
                <option value="false">Not Ready</option>
              </select>

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
                {tableHeaders.map((item, index) => (
                  <th key={index}>{item}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {Array.isArray(products) && products.length > 0 ? (
                products.map((item, index) => (
                  <tr key={item.id_product}>
                    <th>{(page - 1) * 10 + ++index}</th>
                    <td>{item.product_name}</td>

                    <td>
                      <p
                        className={` ${
                          item.categories === "FOODS"
                            ? "bg-red-600 w-14 px-2 py-1 text-white rounded-sm text-center"
                            : item.categories === "DRINKS"
                            ? "bg-blue-600 w-14 px-2 py-1 text-white rounded-sm text-center"
                            : ""
                        } font-bold`}>
                        {item.categories === "FOODS"
                          ? "Food"
                          : item.categories === "DRINKS"
                          ? "Drinks"
                          : item.categories}
                      </p>
                    </td>
                    <td>
                      {new Intl.NumberFormat("id-ID", {
                        style: "currency",
                        currency: "IDR",
                      }).format(item.product_price)}
                    </td>

                    <td>{item.user.name}</td>
                    <td>{item.available_stock ? "Ready" : "Not Ready"}</td>
                    <td className="flex gap-8">
                      <button
                        className="tooltip"
                        data-tip="Edit"
                        onClick={() => onButtonEditClick(item.id_product)}>
                        <SquarePen size={28} color="#00d15b" />
                      </button>
                      <button
                        onClick={() => handleDelete(item.id_product)}
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
                    No products available
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

export default Products;
