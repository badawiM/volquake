import { PriceRequestPage } from './app.po';

describe('price-request App', function() {
  let page: PriceRequestPage;

  beforeEach(() => {
    page = new PriceRequestPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
