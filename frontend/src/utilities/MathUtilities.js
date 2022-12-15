class MathUtilities {
  // eslint-disable-next-line class-methods-use-this
  uuid() {
    return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, (c) =>
      // eslint-disable-next-line no-bitwise,no-mixed-operators,implicit-arrow-linebreak
      (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16));
  }

  // eslint-disable-next-line class-methods-use-this
  mmHHtoMin(timeString) {
    if (timeString === undefined || timeString === null) return -1;
    const timeArray = timeString.split(':');
    // eslint-disable-next-line radix
    let time = parseInt(timeArray[0]) * 60 + parseInt(timeArray[1]);
    if (time < 60) time += 24 * 60;
    return time;
  }
}

export default new MathUtilities();
