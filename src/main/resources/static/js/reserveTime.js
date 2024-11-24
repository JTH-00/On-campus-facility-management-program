const nonClick=document.querySelectorAll(".non-click");
var giMenuDuration=700;

function handleClick(event) {
    // div에서 모든 clik 제거한다.
    nonClick.forEach((e) => {
        e.classList.remove("click");
    });
    //클릭한 div만 "click" 클래스 추가
    event.target.classList.add("click");
}

nonClick.forEach((e)=>{
    e.addEventListener("click",handleClick);
});

$(document).ready(function() {
    var slides = $('.slide');
    var slideIndex = 0;
  
    function showSlide() {
        slides.removeClass('active');
        slides.eq(slideIndex).addClass('active');
    }
  
    function nextSlide() {
        slideIndex++;
        if (slideIndex >= slides.length) {
            slideIndex = 0;
        }
        showSlide();
    }
  
    function prevSlide() {
        slideIndex--;
        if (slideIndex < 0) {
            slideIndex = slides.length - 1;
        }
        showSlide();
    }
  
    $('.prev').click(function() {
        prevSlide();
    });
  
    $('.next').click(function() {
        nextSlide();
    });
  
    setInterval(nextSlide, 5000);
});

const hamburgerMenu = document.querySelector(".hamburger-menu");
const panel = document.querySelector(".panel");

hamburgerMenu.addEventListener("click", function() {
  this.classList.toggle("active");
  panel.classList.toggle("active");
});

function closePanel() {
    document.getElementById('menu-panel').style.transform = 'translateX(-100%)';
    document.body.style.overflow = 'visible'; // 스크롤 다시 허용
    menuButton.setAttribute('aria-expanded', 'false');
}