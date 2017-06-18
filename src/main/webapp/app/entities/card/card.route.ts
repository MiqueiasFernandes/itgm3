import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CardComponent } from './card.component';
import { CardDetailComponent } from './card-detail.component';
import { CardPopupComponent } from './card-dialog.component';
import { CardDeletePopupComponent } from './card-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CardResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const cardRoute: Routes = [
  {
    path: 'card',
    component: CardComponent,
    resolve: {
      'pagingParams': CardResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.card.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'card/:id',
    component: CardDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.card.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const cardPopupRoute: Routes = [
  {
    path: 'card-new',
    component: CardPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.card.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'card/:id/edit',
    component: CardPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.card.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'card/:id/delete',
    component: CardDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.card.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
