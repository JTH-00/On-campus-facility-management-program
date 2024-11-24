
const urlParams = new URLSearchParams(window.location.search);
const error = urlParams.get('error');

// error 값에 따라 알림창을 표시
if (error === 'duplicated') {
    alert('이미 등록된 날짜입니다.');
}else if (error === 'unknown') {
    alert('알 수 없는 오류가 발생했습니다.');
}