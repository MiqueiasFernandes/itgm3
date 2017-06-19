import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ItgmSharedModule } from '../../shared';
import {
    PrognoseService,
    PrognosePopupService,
    PrognoseComponent,
    PrognoseDetailComponent,
    PrognoseDialogComponent,
    PrognosePopupComponent,
    PrognoseDeletePopupComponent,
    PrognoseDeleteDialogComponent,
    prognoseRoute,
    prognosePopupRoute,
    PrognoseResolvePagingParams,
} from './';
import { FabAddPrognoseComponent } from './fab-add-prognose/fab-add-prognose.component';

const ENTITY_STATES = [
    ...prognoseRoute,
    ...prognosePopupRoute,
];

@NgModule({
    imports: [
        ItgmSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PrognoseComponent,
        PrognoseDetailComponent,
        PrognoseDialogComponent,
        PrognoseDeleteDialogComponent,
        PrognosePopupComponent,
        PrognoseDeletePopupComponent,
        FabAddPrognoseComponent,
    ],
    entryComponents: [
        PrognoseComponent,
        PrognoseDialogComponent,
        PrognosePopupComponent,
        PrognoseDeleteDialogComponent,
        PrognoseDeletePopupComponent,
        FabAddPrognoseComponent,
    ],
    providers: [
        PrognoseService,
        PrognosePopupService,
        PrognoseResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ItgmPrognoseModule {}
