import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import {
    CardService,
    CardPopupService,
    CardComponent,
    CardDetailComponent,
    CardDialogComponent,
    CardPopupComponent,
    CardDeletePopupComponent,
    CardDeleteDialogComponent,
    cardRoute,
    cardPopupRoute,
    CardResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cardRoute,
    ...cardPopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CardComponent,
        CardDetailComponent,
        CardDialogComponent,
        CardDeleteDialogComponent,
        CardPopupComponent,
        CardDeletePopupComponent,
    ],
    entryComponents: [
        CardComponent,
        CardDialogComponent,
        CardPopupComponent,
        CardDeleteDialogComponent,
        CardDeletePopupComponent,
    ],
    providers: [
        CardService,
        CardPopupService,
        CardResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmCardModule {}
