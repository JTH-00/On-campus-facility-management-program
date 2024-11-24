const modal = document.querySelector(".modal-overlay");
const body = document.querySelector("body");

function modalOn() {
    body.style.overflow = "hidden";
    // 스크롤막기
    modal.style.display = "flex";
    // 모달보이게하기
}

function isModalOn() {
    return modal.style.display === "flex";
}

function modalOff() {
    modal.style.display = "none";
    body.style.overflow = "visible";
    // 모달안보이게
}

const btnModal = document.querySelectorAll(".btn-modal");

btnModal.forEach(btn => {
  btn.addEventListener("click", e => {
    modalOn();
  });
});

const closeBtn = modal.querySelector(".close-area");
closeBtn.addEventListener("click", e => {
    modalOff();
    // x누름
});

window.addEventListener("keyup", e => {
    if (isModalOn() && e.key === "Escape") {
        modalOff();
        // esc
    }
});